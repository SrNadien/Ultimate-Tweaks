package nadiendev.ultimatetweaks.client.discord;

public record QuestProgress(int done, int total) {

    public boolean isAvailable() {
        return total > 0 && done >= 0 && done <= total;
    }
}
