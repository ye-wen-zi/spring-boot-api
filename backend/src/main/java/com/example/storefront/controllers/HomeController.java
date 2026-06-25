package com.example.storefront.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.services.ProductService;

@RestController
@RequestMapping("/")
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Iterable<ProductDetailResponse> home() {
        // return List.of("JavaScript", "Ruby");
        return this.productService.getAllProductDetails();
    }
}
