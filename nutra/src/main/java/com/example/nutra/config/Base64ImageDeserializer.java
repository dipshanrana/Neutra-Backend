package com.example.nutra.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Base64;

public class Base64ImageDeserializer extends JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }

        // Strip the Data URL prefix if present (e.g., "data:image/png;base64,")
        if (value.contains(",")) {
            value = value.split(",")[1];
        }

        try {
            return Base64.getDecoder().decode(value);
        } catch (IllegalArgumentException e) {
            // If decoding fails, try decoding with MIME decoder (handles line breaks)
            return Base64.getMimeDecoder().decode(value);
        }
    }
}
