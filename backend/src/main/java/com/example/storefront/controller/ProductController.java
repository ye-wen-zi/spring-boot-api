package com.example.storefront.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.dto.ProductResponse;
import com.example.storefront.dto.ProductUpdateRequest;
import com.example.storefront.service.ProductService;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    Iterable<ProductResponse> find() {
        return this.productService.find();
    }

    @GetMapping("/{id}")
    ProductDetailResponse findById(@PathVariable UUID id) {
        return this.productService.findProductById(id);
    }

    @PostMapping
    public ResponseEntity<ProductDetailResponse> create(/** @Valid */
    @RequestBody ProductCreateRequest productDto) {
        ProductDetailResponse product = this.productService.save(productDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.id())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> updateProduct(
            @PathVariable UUID id,
            // @Valid
            @RequestBody ProductUpdateRequest productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content công nhận đã xóa
    }
}
