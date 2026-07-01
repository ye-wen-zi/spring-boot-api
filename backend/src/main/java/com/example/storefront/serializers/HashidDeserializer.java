package com.example.storefront.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent // Đăng ký với Jackson
public class HashidDeserializer extends JsonDeserializer<Long> {

    @Autowired
    private Hashids hashids;

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText().trim();
        if (text.isEmpty()) {
            return null;
        }
        try {
            long[] decoded = hashids.decode(text);
            return decoded.length > 0 ? decoded[0] : null;
        } catch (Exception e) {
            throw new IOException("Giá trị Hashid không hợp lệ: " + text);
        }
    }
}