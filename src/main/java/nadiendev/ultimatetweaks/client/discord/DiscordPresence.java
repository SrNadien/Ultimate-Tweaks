package nadiendev.ultimatetweaks.client.discord;

import com.google.gson.JsonObject;
import nadiendev.ultimatetweaks.UltimateTweaksCore;
import nadiendev.ultimatetweaks.config.TweaksClientConfig;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

public final class DiscordPresence {

    private static final Logger LOGGER = UltimateTweaksCore.LOGGER;

    private static final long RETRY_MS = 10_000L;
    private static final long POLL_MS = 1_000L;
    private static final int SETTINGS_REFRESH_TICKS = 20;
    private static final int QUESTS_REFRESH_TICKS = 100;

    private static final Object LOCK = new Object();

    private static Thread worker;
    private static int ticks;

    private static volatile PresenceSettings settings = PresenceSettings.defaults();
    private static volatile PresenceStatus status = PresenceStatus.STARTING;
    private static volatile String dimension = "";
    private static volatile QuestProgress quests;
    private static volatile int rotationSeconds = PresenceSettings.DEFAULT_ROTATION_SECONDS;
    private static volatile int maxLineLength = PresenceSettings.DEFAULT_MAX_LINE_LENGTH;
    private static volatile String applicationId = PresenceSettings.APPLICATION_ID;
    private static volatile boolean running;
    private static final IconProbe ICON_PROBE = new IconProbe();
    private static final DiscordAssets ASSETS = new DiscordAssets();
    private static volatile String smallImageSetting = "";
    private static volatile DiscordIpc current;

    private DiscordPresence() {
    }

    public static void start() {
        synchronized (LOCK) {
            if (worker != null) {
                return;
            }

            refreshSettings();
            running = true;
            worker = new Thread(DiscordPresence::run, "UltimateTweaks-Discord");
            worker.setDaemon(true);
            worker.start();

            Runtime.getRuntime().addShutdownHook(new Thread(DiscordPresence::shutdown, "UltimateTweaks-DiscordShutdown"));
        }
    }

    public static void onClientTick() {
        start();

        if (ticks++ % SETTINGS_REFRESH_TICKS == 0) {
            refreshSettings();
        }
        status = resolveStatus();
        dimension = resolveDimension();

        if (ticks % QUESTS_REFRESH_TICKS == 0) {
            QuestProgress read = readQuests();
            if (read != null) {
                quests = read;
            }
        }
    }

    private static QuestProgress readQuests() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || !ModList.get().isLoaded("ftbquests")) {
            return null;
        }
        return FtbQuestsProgress.read(minecraft.player);
    }

    private static String resolveDimension() {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return "";
        }

        ResourceLocation id = level.dimension().location();
        return PresenceDimension.displayName(id.getNamespace(), id.getPath());
    }

    private static void shutdown() {
        running = false;
        DiscordIpc ipc = current;
        current = null;
        if (ipc != null) {
            try {
                ipc.clearActivity();
            } catch (IOException ignored) {
            }
            ipc.close();
        }
    }

    private static void refreshSettings() {
        if (!TweaksClientConfig.SPEC.isLoaded()) {
            return;
        }

        applicationId = PresenceSettings.resolveApplicationId(TweaksClientConfig.DISCORD_APPLICATION_ID.get());
        rotationSeconds = TweaksClientConfig.DISCORD_ROTATION_SECONDS.get();
        maxLineLength = TweaksClientConfig.DISCORD_MAX_LINE_LENGTH.get();
        smallImageSetting = TweaksClientConfig.DISCORD_SMALL_IMAGE.get().trim();
        settings = new PresenceSettings(
                TweaksClientConfig.DISCORD_ENABLED.get(),
                TweaksClientConfig.SOURCE_CURSEFORGE.equals(TweaksClientConfig.DISCORD_SOURCE.get()),
                TweaksClientConfig.DISCORD_DETAILS.get(),
                new ArrayList<>(TweaksClientConfig.DISCORD_STATES.get()),
                TweaksClientConfig.DISCORD_LARGE_IMAGE.get().trim(),
                TweaksClientConfig.DISCORD_LARGE_IMAGE_TEXT.get(),
                TweaksClientConfig.DISCORD_SHOW_ELAPSED.get(),
                TweaksClientConfig.DISCORD_CURSEFORGE_BUTTON.get(),
                TweaksClientConfig.DISCORD_INVITE_URL.get().trim(),
                TweaksClientConfig.DISCORD_STATUS_STARTING.get(),
                TweaksClientConfig.DISCORD_STATUS_MENU.get(),
                TweaksClientConfig.DISCORD_STATUS_SINGLEPLAYER.get(),
                TweaksClientConfig.DISCORD_STATUS_MULTIPLAYER.get(),
                TweaksClientConfig.DISCORD_MANUAL_NAME.get().trim(),
                TweaksClientConfig.DISCORD_MANUAL_VERSION.get().trim(),
                TweaksClientConfig.DISCORD_MANUAL_AUTHOR.get().trim(),
                TweaksClientConfig.DISCORD_MANUAL_URL.get().trim());
    }

    private static PresenceStatus resolveStatus() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.level != null && minecraft.player != null) {
            return minecraft.getSingleplayerServer() != null
                    ? PresenceStatus.SINGLEPLAYER
                    : PresenceStatus.MULTIPLAYER;
        }
        if (minecraft.getOverlay() != null || minecraft.screen == null) {
            return PresenceStatus.STARTING;
        }
        return PresenceStatus.MENU;
    }

    private static void run() {
        long startSeconds = ManagementFactory.getRuntimeMXBean().getStartTime() / 1000L;
        String minecraftVersion = SharedConstants.getCurrentVersion().getName();
        PackInfo curseforge = PackInfo.load(FMLPaths.GAMEDIR.get());

        String lastPayload = null;
        String connectedApplication = null;
        long nextAttempt = 0L;

        while (running) {
            try {
                PresenceSettings snapshot = settings;

                String id = applicationId;

                if (!snapshot.enabled() || id.isBlank()) {
                    disconnect(true);
                    lastPayload = null;
                } else {
                    if (current != null && !id.equals(connectedApplication)) {
                        disconnect(true);
                        lastPayload = null;
                    }

                    if (current == null && System.currentTimeMillis() >= nextAttempt) {
                        try {
                            current = DiscordIpc.connect(id);
                            connectedApplication = id;
                            lastPayload = null;
                            LOGGER.info("Discord Rich Presence conectado");
                        } catch (IOException exception) {
                            nextAttempt = System.currentTimeMillis() + RETRY_MS;
                            LOGGER.debug("Discord no disponible: {}", exception.getMessage());
                        }
                    }

                    DiscordIpc ipc = current;
                    if (ipc != null) {
                        PackInfo pack = PresenceActivity.resolvePack(snapshot, curseforge, minecraftVersion);
                        int frame = rotationSeconds <= 0
                                ? -1
                                : (int) (System.currentTimeMillis() / 1000L / rotationSeconds);
                        JsonObject activity = PresenceActivity.build(snapshot, pack, status, dimension,
                                ModList.get().size(), quests, minecraftVersion, startSeconds, frame,
                                DiscordPresence::smallImageFor,
                                maxLineLength);
                        String payload = activity.toString();

                        if (!payload.equals(lastPayload)) {
                            JsonObject reply = ipc.setActivity(activity);
                            LOGGER.debug("Discord Rich Presence enviado: {}", payload);
                            LOGGER.debug("Discord Rich Presence respuesta: {}", reply);
                            lastPayload = payload;
                        }
                    }
                }
            } catch (IOException exception) {
                disconnect(false);
                lastPayload = null;
                nextAttempt = System.currentTimeMillis() + RETRY_MS;
                LOGGER.debug("Discord Rich Presence desconectado: {}", exception.getMessage());
            } catch (RuntimeException exception) {
                disconnect(false);
                lastPayload = null;
                nextAttempt = System.currentTimeMillis() + RETRY_MS;
                LOGGER.warn("Discord Rich Presence fallo", exception);
            }

            try {
                Thread.sleep(POLL_MS);
            } catch (InterruptedException exception) {
                return;
            }
        }
    }

    private static String smallImageFor(String iconName) {
        String configured = smallImageSetting;
        if (!configured.isEmpty()) {
            return configured;
        }
        if (ASSETS.has(applicationId, iconName)) {
            return iconName;
        }
        String url = PresenceSettings.DIMENSION_ICON_BASE + iconName + ".png";
        return ICON_PROBE.isReachable(url) ? url : "";
    }

    private static void disconnect(boolean clear) {
        DiscordIpc ipc = current;
        current = null;
        if (ipc == null) {
            return;
        }
        if (clear) {
            try {
                ipc.clearActivity();
            } catch (IOException ignored) {
            }
        }
        ipc.close();
    }
}
