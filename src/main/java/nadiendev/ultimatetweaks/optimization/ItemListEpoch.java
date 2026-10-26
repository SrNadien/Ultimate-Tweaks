package nadiendev.ultimatetweaks.optimization;

public final class ItemListEpoch {

    private static volatile int epoch;

    private ItemListEpoch() {
    }

    public static int current() {
        return epoch;
    }

    public static void invalidate() {
        epoch++;
    }
}
