package com.example.storefront.resolvers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // Chỉ dùng cho tham số ở Controller
@Retention(RetentionPolicy.RUNTIME)
public @interface HashId {
}
