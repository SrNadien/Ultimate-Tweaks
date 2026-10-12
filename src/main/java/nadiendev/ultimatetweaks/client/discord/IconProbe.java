package nadiendev.ultimatetweaks.client.discord;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

public final class IconProbe {

    private static final Duration TIMEOUT = Duration.ofSeconds(4);
    private static final long FOUND_TTL_MS = 6L * 60L * 60L * 1000L;
    private static final long MISSING_TTL_MS = 10L * 60L * 1000L;

    private record Entry(boolean reachable, long expiresAt) {
    }

    private final ConcurrentHashMap<String, Entry> cache = new ConcurrentHashMap<>();
    private final LongSupplier clock;
    private final HttpClient client;

    public IconProbe() {
        this(System::currentTimeMillis);
    }

    IconProbe(LongSupplier clock) {
        this.clock = clock;
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public boolean isReachable(String url) {
        long now = clock.getAsLong();
        Entry cached = cache.get(url);
        if (cached != null && cached.expiresAt() > now) {
            return cached.reachable();
        }

        boolean reachable = check(url);
        cache.put(url, new Entry(reachable, now + (reachable ? FOUND_TTL_MS : MISSING_TTL_MS)));
        return reachable;
    }

    private boolean check(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(TIMEOUT)
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() >= 200 && response.statusCode() < 300;
        } catch (IOException | IllegalArgumentException exception) {
            return false;
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
