package com.example.storefront.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtils {

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String slug = input.toLowerCase();

        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(slug).replaceAll("");

        slug = slug.replaceAll("[^a-z0-9\\s-]", "");
        slug = slug.replaceAll("\\s+", "-");
        slug = slug.replaceAll("-+", "-");

        return slug;
    }
}