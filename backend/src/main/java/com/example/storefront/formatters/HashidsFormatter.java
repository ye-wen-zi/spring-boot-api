package com.example.storefront.formatters;

import java.util.Locale;

import org.hashids.Hashids;
import org.springframework.format.Formatter;

import com.example.storefront.exceptions.InvalidHashException;

public class HashidsFormatter implements Formatter<Long> {

    private final Hashids hashids;

    public HashidsFormatter(Hashids hashids) {
        this.hashids = hashids;
    }

    @Override
    public String print(Long object, Locale locale) {
        return this.hashids.encode(object);
    }

    @Override
    public Long parse(String text, Locale locale) throws InvalidHashException {
        long[] decoded = this.hashids.decode(text);

        if (decoded.length == 0) {
            // throw new IllegalArgumentException("ID is invalid!");
            throw new InvalidHashException("ID is invalid!");
        }
        return decoded[0];
    }

}
