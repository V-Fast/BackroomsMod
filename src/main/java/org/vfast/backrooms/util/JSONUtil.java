package org.vfast.backrooms.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

// Taken from: https://github.com/devs-immortal/Paradise-Lost/blob/2.2.x/1.20.0/master/src/main/java/net/id/paradiselost/util/MiscUtil.java

public class JSONUtil {
    /**
     * Deserializes a JSON file that is stored in the "data" directory of this mod.
     *
     * @param codec    The coded to use to deserialize the JSON
     * @param resource The identifier of the JSON
     * @param <T>      The type of data to deserialize
     * @return The deserialized JSON
     * @throws IOException If there was a problem reading or parsing the JSON
     */
    public static <T> T deserializeDataJson(Codec<T> codec, Identifier resource) throws IOException {
        return deserializeDataJson(JsonOps.INSTANCE, codec, resource);
    }

    /**
     * Deserializes a JSON file that is stored in the "data" directory of this mod.
     *
     * @param ops      The ops to use instead of the default JSON ops
     * @param codec    The coded to use to deserialize the JSON
     * @param resource The identifier of the JSON
     * @param <T>      The type of data to deserialize
     * @return The deserialized JSON
     * @throws IOException If there was a problem reading or parsing the JSON
     */
    public static <T> T deserializeDataJson(DynamicOps<JsonElement> ops, Codec<T> codec, Identifier resource) throws IOException {
        var resourcePath = "/data/" + resource.getNamespace() + '/' + resource.getPath() + ".json";
        var stream = JSONUtil.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new FileNotFoundException("Failed to locate data JSON: " + resource);
        }
        try (stream) {
            var reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            var decodeResult = codec.decode(ops, JsonHelper.deserialize(reader));

            var result = decodeResult.result();
            if (result.isPresent()) {
                return result.get().getFirst();
            } else {
                //noinspection OptionalGetWithoutIsPresent
                throw new IOException(decodeResult.error().get().message());
            }
        }
    }
}
