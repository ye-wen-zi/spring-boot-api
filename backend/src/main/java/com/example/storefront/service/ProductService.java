package com.example.storefront.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.dto.ProductResponse;
import com.example.storefront.dto.ProductUpdateRequest;
import com.example.storefront.entity.AssignedVariantAttribute;
import com.example.storefront.entity.Attribute;
import com.example.storefront.entity.AttributeValue;
import com.example.storefront.entity.Category;
import com.example.storefront.entity.Product;
import com.example.storefront.entity.ProductType;
import com.example.storefront.entity.ProductVariant;
import com.example.storefront.exception.BadRequestException;
import com.example.storefront.exception.ResourceNotFoundException;
import com.example.storefront.mappers.ProductMapper;
import com.example.storefront.repositories.AttributeRepository;
import com.example.storefront.repositories.AttributeValueRepository;
import com.example.storefront.repositories.CategoryRepository;
import com.example.storefront.repositories.ProductRepository;
import com.example.storefront.repositories.ProductTypeRepository;
import com.example.storefront.utils.SlugUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductTypeRepository productTypeRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper,
            ProductTypeRepository productTypeRepository, CategoryRepository categoryRepository,
            AttributeValueRepository attributeValueRepository,
            AttributeRepository attributeRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productTypeRepository = productTypeRepository;
        this.categoryRepository = categoryRepository;
        this.attributeValueRepository = attributeValueRepository;
        this.attributeRepository = attributeRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> find() {
        List<Product> products = this.productRepository.findAll();
        return productMapper.toResponseList(products);
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getAllProductDetails() {
        List<Product> products = productRepository.findAllWithProductType();
        return productMapper.toDetailResponseList(products);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse findProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found!"));
        return productMapper.toDetailResponse(product);
    }

    @Transactional
    public ProductDetailResponse save(ProductCreateRequest productDto) {
        Category category = this.categoryRepository
                .findById(productDto.categoryId())
                .orElseThrow(() -> new BadRequestException("The category does not exist!"));
        ProductType productType = this.productTypeRepository
                .findById(productDto.typeId())
                .orElseThrow(() -> new BadRequestException("The product type does not exist!"));

        Product product = this.productMapper.toEntity(productDto);

        product.setSlug(SlugUtils.toSlug(product.getName()));
        product.setCategory(category);
        product.setProductType(productType);
        List<ProductVariant> variants = product.getVariants();

        // Tránh product_id = null khi lưu và db
        if (variants != null) {
            variants.forEach(variant -> variant.setProduct(product));
            product.setMinPrice(
                    variants.stream().map(ProductVariant::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            product.setMaxPrice(
                    variants.stream().map(ProductVariant::getPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
        }

        for (int i = 0; i < product.getVariants().size(); i++) {
            ProductCreateRequest.Variant variantDto = productDto.variants().get(i);
            ProductVariant variantEntity = product.getVariants().get(i);

            variantEntity.setAssignedAttributes(new ArrayList<>());

            List<UUID> attibuteValueIds = variantDto.attributes()
                    .stream()
                    .filter(attr -> attr.attributeValueId() != null)
                    .map(attr -> attr.attributeValueId())
                    .toList();
            // List<AttributeValue> availableAttributeValues =
            // this.attributeValueRepository.findAllById(attibuteValueIds);

            List<AttributeValue> availableAttributeValues = this.attributeValueRepository
                    .findAllById(attibuteValueIds);
            List<ProductCreateRequest.Variant.Attribute> newAttributeValuesReq = variantDto.attributes()
                    .stream()
                    .filter(attr -> attr.attributeValueId() == null)
                    .toList();

            Map<UUID, Attribute> attributes = this.attributeRepository
                    .findAllById(newAttributeValuesReq.stream().map(attr -> attr.attributeId()).toList())
                    .stream()
                    .collect(Collectors.toMap(Attribute::getId, attr -> attr));

            List<AttributeValue> newAttributeValues = new ArrayList<>();

            for (var j = 0; j < newAttributeValuesReq.size(); j++) {
                var currentValue = newAttributeValuesReq.get(j);
                AttributeValue attributeValue = AttributeValue.builder()
                        .attribute(attributes.get(currentValue.attributeId()))
                        .name(currentValue.value())
                        .slug(SlugUtils.toSlug(currentValue.value()))
                        .build();
                newAttributeValues.add(attributeValue);
            }

            this.attributeValueRepository.saveAll(newAttributeValues);

            List<AttributeValue> allAttributeValues = new ArrayList<>(availableAttributeValues);
            allAttributeValues.addAll(newAttributeValues);

            for (var value : allAttributeValues) {
                AssignedVariantAttribute assignedVariantAttribute = AssignedVariantAttribute.builder()
                        .variant(variantEntity)
                        .attribute(value.getAttribute())
                        .selectedValues(List.of(value))
                        .build();

                variantEntity.getAssignedAttributes().add(assignedVariantAttribute);
            }

        }

        this.productRepository.save(product);
        return this.productMapper.toDetailResponse(product);
    }

    public ProductDetailResponse update(UUID id, ProductUpdateRequest productDTO) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }
}