package com.example.storefront.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.storefront.dto.ProductTypeCreateRequest;
import com.example.storefront.entities.ProductType;
import com.example.storefront.exceptions.BadRequestException;
import com.example.storefront.repositories.ProductRepository;
import com.example.storefront.repositories.ProductTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final ProductRepository productRepository;

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
        if (this.productRepository.existsByProductTypeId(id)) {
            throw new BadRequestException("Cannot delete because the type already has products.");
        }
        this.productTypeRepository.deleteById(id);
    }
}
