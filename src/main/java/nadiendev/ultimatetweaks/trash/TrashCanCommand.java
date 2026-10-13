package nadiendev.ultimatetweaks.trash;

import nadiendev.ultimatetweaks.UltimateTweaksCore;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = UltimateTweaksCore.MOD_ID)
public final class TrashCanCommand {

    public static final String ROOT = UltimateTweaksCore.MOD_ID;
    public static final String NAME = "trashcan";
    public static final String CLEAR = "clear";
    public static final String LINE = ROOT + " " + NAME;
    public static final String CLEAR_LINE = LINE + " " + CLEAR;

    private TrashCanCommand() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal(ROOT)
                .then(Commands.literal(NAME)
                        .executes(context -> {
                            TrashCan.open(context.getSource().getPlayerOrException());
                            return 1;
                        })
                        .then(Commands.literal(CLEAR).executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            if (player.containerMenu instanceof TrashCanMenu menu) {
                                menu.clearTrash();
                                return 1;
                            }
                            return 0;
                        }))));
    }
}
