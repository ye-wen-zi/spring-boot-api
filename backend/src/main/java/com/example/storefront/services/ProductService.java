package com.example.storefront.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.dto.ProductResponse;
import com.example.storefront.dto.ProductUpdateRequest;
import com.example.storefront.entities.AssignedVariantAttribute;
import com.example.storefront.entities.Attribute;
import com.example.storefront.entities.AttributeValue;
import com.example.storefront.entities.Category;
import com.example.storefront.entities.Product;
import com.example.storefront.entities.ProductType;
import com.example.storefront.entities.ProductVariant;
import com.example.storefront.exceptions.BadRequestException;
import com.example.storefront.exceptions.ResourceNotFoundException;
import com.example.storefront.mappers.ProductMapper;
import com.example.storefront.repositories.AssignedAttributeRepository;
import com.example.storefront.repositories.AttributeRepository;
import com.example.storefront.repositories.AttributeValueRepository;
import com.example.storefront.repositories.CategoryRepository;
import com.example.storefront.repositories.ProductRepository;
import com.example.storefront.repositories.ProductTypeRepository;
import com.example.storefront.repositories.ProductVariantRepository;
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
            AttributeRepository attributeRepository,
            AssignedAttributeRepository assignedAttributeRepository,
            ProductVariantRepository productVariantRepository) {
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
        return productMapper.fromEntitiesToResponses(products);
    }

    @Transactional(readOnly = true)
    public List<ProductDetailResponse> getAllProductDetails() {
        List<Product> products = productRepository.findAllWithProductType();
        return productMapper.fromEntityListToDetailResponseList(products);
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse findProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found!"));
        return productMapper.fromEntityToDetailResponse(product);
    }

    private void assignCategory(Product product, UUID categoryId) {
        Category category = this.categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new BadRequestException("The category does not exist!"));
        product.setCategory(category);
    }

    private void assignProductType(Product product, UUID typeId) {
        ProductType productType = this.productTypeRepository
                .findById(typeId)
                .orElseThrow(() -> new BadRequestException("The product type does not exist!"));
        product.setProductType(productType);
    }

    private void linkProductToVariants(Product product) {
        // Tránh product_id = null khi lưu và db
        var variants = product.getVariants();
        if (variants != null) {
            variants.forEach(variant -> variant.setProduct(product));
        }
    }

    private void assignMinMaxPrice(Product product) {
        var variants = product.getVariants();
        if (variants != null) {
            product.setMinPrice(
                    variants.stream().map(ProductVariant::getPrice).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            product.setMaxPrice(
                    variants.stream().map(ProductVariant::getPrice).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
        }
    }

    @Transactional
    public ProductDetailResponse save(ProductCreateRequest productDto) {

        Product product = this.productMapper.fromCreateRequestToEntity(productDto);

        product.setSlug(SlugUtils.toSlug(product.getName()));
        assignCategory(product, productDto.categoryId());
        assignProductType(product, productDto.typeId());
        assignMinMaxPrice(product);

        linkProductToVariants(product);

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

            List<AttributeValue> attributeValues = this.attributeValueRepository
                    .findAllById(attibuteValueIds);

            for (var value : attributeValues) {
                AssignedVariantAttribute assignedVariantAttribute = AssignedVariantAttribute.builder()
                        .variant(variantEntity)
                        .attribute(value.getAttribute())
                        .value(value)
                        .build();

                variantEntity.getAssignedAttributes().add(assignedVariantAttribute);
            }

        }
        this.productRepository.save(product);
        return this.productMapper.fromEntityToDetailResponse(product);
    }

    @Transactional
    public ProductDetailResponse update(Long id, ProductUpdateRequest productDto) {
        var product = this.productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product does not exist: " + id.toString()));

        this.productMapper.fromUpdateRequestToEntity(productDto, product);

        if (!product.getCategory().getId().equals(productDto.categoryId())) {
            assignCategory(product, productDto.categoryId());
        }

        if (!product.getProductType().getId().equals(productDto.typeId())) {
            assignProductType(product, productDto.typeId());
        }

        product.setSlug(SlugUtils.toSlug(productDto.name()));

        Map<UUID, ProductVariant> variantsMap = product.getVariants()
                .stream()
                .collect(Collectors.toMap(ProductVariant::getId, v -> v));

        // Danh sách để giữ lại các Variant active (cũ được update + mới được thêm)
        List<ProductVariant> updatedVariants = new ArrayList<>();

        if (productDto.variants() != null) {

            var attributeIds = productDto.variants().stream()
                    .flatMap(v -> v.attributes().stream().map(attr -> attr.attributeId())).toList();

            Map<UUID, Attribute> attributesMap = this.attributeRepository.findAllById(attributeIds)
                    .stream().collect(Collectors.toMap(Attribute::getId, attr -> attr));

            var attributeValueIds = productDto.variants().stream()
                    .flatMap(v -> v.attributes().stream().map(attr -> attr.attributeValueId())).toList();

            Map<UUID, AttributeValue> attributeValuesMap = this.attributeValueRepository.findAllById(attributeValueIds)
                    .stream().collect(Collectors.toMap(AttributeValue::getId, v -> v));

            for (var variantDto : productDto.variants()) {
                boolean isNewVariant = variantDto.id() == null;
                ProductVariant variant = null;

                if (!isNewVariant) {
                    if (!variantsMap.containsKey(variantDto.id())) {
                        throw new BadRequestException("Invalid variant!");
                    }
                    variant = variantsMap.get(variantDto.id());
                }

                if (variant == null) {
                    variant = ProductVariant.builder()
                            .product(product)
                            .assignedAttributes(new ArrayList<>())
                            .build();
                }

                this.productMapper.fromUpdateVariantToVariantEntity(variantDto, variant);

                Map<UUID, AssignedVariantAttribute> assignedAttributesMap = variant.getAssignedAttributes()
                        .stream().collect(Collectors.toMap(AssignedVariantAttribute::getId, attr -> attr));

                List<AssignedVariantAttribute> updatedAssignedAttributes = new ArrayList<>();

                for (var assignedDto : variantDto.attributes()) {
                    boolean isNewAssignedAttribute = assignedDto.assignedId() == null;
                    AssignedVariantAttribute assignedVariantAttribute = null;

                    if (!isNewAssignedAttribute) {
                        if (!assignedAttributesMap.containsKey(assignedDto.assignedId())) {
                            throw new BadRequestException("Invalid attribute");
                        }
                        assignedVariantAttribute = assignedAttributesMap.get(assignedDto.assignedId());
                    }

                    if (assignedVariantAttribute == null) {
                        assignedVariantAttribute = AssignedVariantAttribute.builder()
                                .variant(variant)
                                .build();
                    }

                    assignedVariantAttribute.setAttribute(attributesMap.get(assignedDto.attributeId()));
                    assignedVariantAttribute.setValue(attributeValuesMap.get(assignedDto.attributeValueId()));

                    // Luôn luôn giữ lại thuộc tính này
                    updatedAssignedAttributes.add(assignedVariantAttribute);

                    if (!isNewAssignedAttribute) {
                        // Xóa khỏi map để map chỉ còn lại những ID cần xóa
                        assignedAttributesMap.remove(assignedDto.assignedId());
                    }
                }

                // // 1. Xóa các Attribute thừa dưới DB
                // if (!assignedAttributesMap.isEmpty()) {
                // this.assignedAttributeRepository.deleteAllById(assignedAttributesMap.keySet());
                // }

                // 2. Cập nhật lại Collection của Java để Hibernate không thấy hàng "stale" (đã
                // xóa)
                variant.getAssignedAttributes().clear();
                variant.getAssignedAttributes().addAll(updatedAssignedAttributes);

                // Đưa variant này vào danh sách giữ lại của Product
                updatedVariants.add(variant);

                if (!isNewVariant) {
                    // Xóa khỏi map để map chỉ còn lại các Variant cần xóa
                    variantsMap.remove(variantDto.id());
                }
            }

            assignMinMaxPrice(product);

            // // 3. Xóa các Variant thừa dưới DB
            // if (!variantsMap.isEmpty()) {
            // this.productVariantRepository.deleteAllById(variantsMap.keySet());
            // }

            // 4. Cập nhật lại Collection Variants của Product (Dọn sạch variant đã xóa)
            product.getVariants().clear();
            product.getVariants().addAll(updatedVariants);

            // 5. Lưu an toàn mà không sợ lỗi ObjectDeletedException
            this.productRepository.save(product);
        }

        return this.productMapper.fromEntityToDetailResponse(product);
    }

    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}