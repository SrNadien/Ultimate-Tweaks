package nadiendev.ultimatetweaks.client.discord;

import net.minecraft.world.entity.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

public final class FtbQuestsProgress {

    private static final String API_CLASS = "dev.ftb.mods.ftbquests.api.FTBQuestsAPI";
    private static final String API_INTERFACE = "dev.ftb.mods.ftbquests.api.FTBQuestsAPI$API";
    private static final String QUEST_FILE = "dev.ftb.mods.ftbquests.api.QuestFile";
    private static final String TEAM_DATA = "dev.ftb.mods.ftbquests.quest.TeamData";

    private static boolean prepared;
    private static boolean unavailable;

    private static Method api;
    private static Method getQuestFile;
    private static Method getTeamData;
    private static Method forAllQuests;
    private static Method isCompleted;

    private FtbQuestsProgress() {
    }

    public static QuestProgress read(Player player) {
        if (player == null || unavailable) {
            return null;
        }

        try {
            if (!prepared) {
                prepare();
            }

            Object apiInstance = api.invoke(null);
            Object questFile = getQuestFile.invoke(apiInstance, true);
            if (questFile == null) {
                return null;
            }

            Optional<?> teamData = (Optional<?>) getTeamData.invoke(questFile, player);
            if (teamData == null || teamData.isEmpty()) {
                return null;
            }

            Object data = teamData.get();
            int[] counts = new int[2];
            Consumer<Object> counter = quest -> {
                counts[1]++;
                try {
                    if ((boolean) isCompleted.invoke(data, quest)) {
                        counts[0]++;
                    }
                } catch (ReflectiveOperationException | RuntimeException exception) {
                    unavailable = true;
                }
            };
            forAllQuests.invoke(questFile, counter);

            QuestProgress progress = new QuestProgress(counts[0], counts[1]);
            return progress.isAvailable() ? progress : null;
        } catch (InvocationTargetException | IllegalAccessException | RuntimeException exception) {
            return null;
        } catch (ClassNotFoundException | NoSuchMethodException | LinkageError exception) {
            unavailable = true;
            return null;
        }
    }

    private static void prepare() throws ClassNotFoundException, NoSuchMethodException {
        api = Class.forName(API_CLASS).getMethod("api");
        getQuestFile = Class.forName(API_INTERFACE).getMethod("getQuestFile", boolean.class);

        Class<?> questFile = Class.forName(QUEST_FILE);
        getTeamData = questFile.getMethod("getTeamData", Player.class);
        forAllQuests = questFile.getMethod("forAllQuests", Consumer.class);

        Class<?> teamData = Class.forName(TEAM_DATA);
        Method found = null;
        for (Method method : teamData.getMethods()) {
            if (method.getName().equals("isCompleted") && method.getParameterCount() == 1) {
                found = method;
                break;
            }
        }
        if (found == null) {
            throw new NoSuchMethodException("TeamData.isCompleted");
        }
        isCompleted = found;
        prepared = true;
    }
}
