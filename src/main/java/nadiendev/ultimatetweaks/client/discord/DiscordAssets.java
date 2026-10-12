package nadiendev.ultimatetweaks.client.discord;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

public final class DiscordAssets {

    private static final String DEFAULT_BASE = "https://discord.com/api/v10/oauth2/applications";
    private static final Duration TIMEOUT = Duration.ofSeconds(4);
    private static final long FOUND_TTL_MS = 10L * 60L * 1000L;
    private static final long FAILED_TTL_MS = 2L * 60L * 1000L;

    private record Entry(Set<String> names, long expiresAt) {
    }

    private final ConcurrentHashMap<String, Entry> cache = new ConcurrentHashMap<>();
    private final String base;
    private final LongSupplier clock;
    private final HttpClient client;

    public DiscordAssets() {
        this(DEFAULT_BASE, System::currentTimeMillis);
    }

    DiscordAssets(String base, LongSupplier clock) {
        this.base = base;
        this.clock = clock;
        this.client = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public boolean has(String applicationId, String name) {
        if (applicationId == null || applicationId.isBlank() || name == null || name.isBlank()) {
            return false;
        }

        long now = clock.getAsLong();
        Entry cached = cache.get(applicationId);
        if (cached == null || cached.expiresAt() <= now) {
            Set<String> names = fetch(applicationId);
            cached = new Entry(names, now + (names.isEmpty() ? FAILED_TTL_MS : FOUND_TTL_MS));
            cache.put(applicationId, cached);
        }
        return cached.names().contains(name);
    }

    private Set<String> fetch(String applicationId) {
        Set<String> names = new HashSet<>();
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(base + "/" + applicationId + "/assets"))
                    .timeout(TIMEOUT)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return names;
            }

            JsonElement root = JsonParser.parseString(response.body());
            if (root.isJsonArray()) {
                for (JsonElement element : root.getAsJsonArray()) {
                    if (element.isJsonObject() && element.getAsJsonObject().has("name")) {
                        names.add(element.getAsJsonObject().get("name").getAsString());
                    }
                }
            }
        } catch (IOException | RuntimeException exception) {
            return new HashSet<>();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
        return names;
    }
}
