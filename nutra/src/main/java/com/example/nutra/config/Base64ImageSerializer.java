package com.example.nutra.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Base64;

public class Base64ImageSerializer extends JsonSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || value.length == 0) {
            gen.writeNull();
            return;
        }
        // Prepend the data URL prefix
        String base64String = Base64.getEncoder().encodeToString(value);
        gen.writeString("data:image/png;base64," + base64String);
    }
}
