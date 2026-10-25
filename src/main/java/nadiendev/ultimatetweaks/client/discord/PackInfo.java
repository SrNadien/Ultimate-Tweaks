package nadiendev.ultimatetweaks.client.discord;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public record PackInfo(String name, String version, String author, String url, String thumbnail) {

    public static final PackInfo EMPTY = new PackInfo("", "", "", "", "");

    private static final String INSTANCE_FILE = "minecraftinstance.json";

    public static PackInfo load(Path gameDirectory) {
        Path file = gameDirectory.resolve(INSTANCE_FILE);
        if (!Files.isRegularFile(file)) {
            return EMPTY;
        }

        Raw raw = new Raw();
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
             JsonReader json = new JsonReader(reader)) {
            readInstance(json, raw);
        } catch (IOException | RuntimeException exception) {
            return EMPTY;
        }

        PackInfo info = new PackInfo(
                firstNonBlank(raw.packName, raw.manifestName, raw.instanceName),
                raw.manifestVersion,
                firstNonBlank(raw.packAuthor, raw.manifestAuthor),
                webUrl(raw.packUrl),
                webUrl(raw.packThumbnail));
        return info.isEmpty() ? EMPTY : info;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    private static void readInstance(JsonReader json, Raw raw) throws IOException {
        if (json.peek() != JsonToken.BEGIN_OBJECT) {
            return;
        }
        json.beginObject();
        while (json.hasNext()) {
            switch (json.nextName()) {
                case "name" -> raw.instanceName = string(json);
                case "manifest" -> readManifest(json, raw);
                case "installedModpack" -> readModpack(json, raw);
                default -> json.skipValue();
            }
        }
        json.endObject();
    }

    private static void readManifest(JsonReader json, Raw raw) throws IOException {
        if (json.peek() != JsonToken.BEGIN_OBJECT) {
            json.skipValue();
            return;
        }
        json.beginObject();
        while (json.hasNext()) {
            switch (json.nextName()) {
                case "name" -> raw.manifestName = string(json);
                case "version" -> raw.manifestVersion = string(json);
                case "author" -> raw.manifestAuthor = string(json);
                default -> json.skipValue();
            }
        }
        json.endObject();
    }

    private static void readModpack(JsonReader json, Raw raw) throws IOException {
        if (json.peek() != JsonToken.BEGIN_OBJECT) {
            json.skipValue();
            return;
        }
        json.beginObject();
        while (json.hasNext()) {
            switch (json.nextName()) {
                case "name" -> raw.packName = string(json);
                case "primaryAuthor" -> raw.packAuthor = string(json);
                case "webSiteURL" -> raw.packUrl = string(json);
                case "thumbnailUrl" -> raw.packThumbnail = string(json);
                default -> json.skipValue();
            }
        }
        json.endObject();
    }

    private static String string(JsonReader json) throws IOException {
        if (json.peek() == JsonToken.STRING) {
            return json.nextString().trim();
        }
        json.skipValue();
        return "";
    }

    private static String webUrl(String value) {
        return value.startsWith("https://") || value.startsWith("http://") ? value : "";
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }

    private static final class Raw {
        String instanceName = "";
        String manifestName = "";
        String manifestVersion = "";
        String manifestAuthor = "";
        String packName = "";
        String packAuthor = "";
        String packUrl = "";
        String packThumbnail = "";
    }
}
