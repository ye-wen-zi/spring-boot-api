package com.example.storefront.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.storefront.dto.ProductTypeCreateRequest;
import com.example.storefront.entities.ProductType;
import com.example.storefront.repositories.ProductTypeRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    public List<ProductType> find() {
        return this.productTypeRepository.findAll();
    }

    public ProductType create(ProductTypeCreateRequest dto) {
        ProductType productType = ProductType.builder()
                .name(dto.name())
                .build();
        return this.productTypeRepository.save(productType);
    }

    public void delete(UUID id) {
        this.productTypeRepository.deleteById(id);
    }
}
