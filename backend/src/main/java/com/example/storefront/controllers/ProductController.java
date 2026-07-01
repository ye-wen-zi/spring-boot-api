package com.example.storefront.controllers;

import java.net.URI;

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
import com.example.storefront.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Products", description = "APIs related to product management.")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get products.")
    Iterable<ProductResponse> find() {
        return this.productService.find();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by hashed id.")
    ProductDetailResponse findById(@PathVariable @Schema(type = "string", example = "bPx2Md") Long id) {
        return this.productService.findProductById(id);
    }

    @PostMapping
    @Operation(summary = "Create new product.")
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
    @Operation(summary = "Update product by hashed id")
    public ResponseEntity<ProductDetailResponse> updateProduct(
            @PathVariable @Schema(type = "string", example = "bPx2Md") Long id,
            // @Valid
            @RequestBody ProductUpdateRequest productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by hashed id")
    public ResponseEntity<Void> deleteProduct(@PathVariable @Schema(type = "string", example = "bPx2Md") Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build(); // Trả về 204 No Content công nhận đã xóa
    }
}
