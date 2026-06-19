package com.example.storefront.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.storefront.entities.Product;
import com.example.storefront.repositories.ProductRepository;

@RestController("/")
public class HomeController {

    private ProductRepository productRepository;

    HomeController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/")
    public Iterable<Product> home() {
        // return List.of("JavaScript", "Ruby");
        return this.productRepository.findAll();
    }
}
