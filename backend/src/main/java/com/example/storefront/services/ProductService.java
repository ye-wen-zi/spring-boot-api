package com.example.storefront.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.entities.Product;
import com.example.storefront.mappers.ProductMapper;
import com.example.storefront.repositories.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getAllProductDetails() {
        // Bước 1: Gọi repo lấy data bằng JOIN FETCH để gom sạch bảng trong 1 câu SQL
        // duy nhất (Tránh N+1)
        List<Product> products = productRepository.findAllWithProductType();

        // Bước 2: Dùng Mapper biến đổi List Entity lồng nhau sang List DTO phẳng chỉ
        // trong 1 dòng
        return productMapper.toDetailResponseList(products);
    }
}