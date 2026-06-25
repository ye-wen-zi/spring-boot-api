package com.example.storefront.serializers;

import java.io.IOException;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


// @JsonComponent // to apply for all Long field
public class HashidsSerializer extends JsonSerializer<Long> {
    
    @Autowired private Hashids hashids;

    // public HashidsSerializer(Hashids hashids) {
    //     this.hashids = hashids;
    // }

    @Override
    public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(hashids.encode(value));
        } else {
            gen.writeNull();
        }
    }
}
