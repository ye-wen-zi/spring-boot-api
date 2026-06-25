package com.example.storefront.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;

import com.example.storefront.entities.AssignedVariantAttribute;
import com.example.storefront.entities.Attribute;
import com.example.storefront.entities.AttributeValue;
import com.example.storefront.entities.Category;
import com.example.storefront.entities.Product;
import com.example.storefront.entities.ProductType;
import com.example.storefront.entities.ProductVariant;
import com.example.storefront.repositories.AttributeRepository;
import com.example.storefront.repositories.AttributeValueRepository;
import com.example.storefront.repositories.CategoryRepository;
import com.example.storefront.repositories.ProductRepository;
import com.example.storefront.repositories.ProductTypeRepository;

import jakarta.transaction.Transactional;

// @Component
public class _DataInitializer implements CommandLineRunner {

        private final CategoryRepository categoryRepository;
        private final ProductTypeRepository productTypeRepository;
        private final ProductRepository productRepository;
        private final AttributeRepository attributeRepository;
        private final AttributeValueRepository attributeValueRepository;

        public _DataInitializer(
                        CategoryRepository categoryRepository,
                        ProductTypeRepository productTypeRepository,
                        ProductRepository productRepository,
                        AttributeRepository attributeRepository,
                        AttributeValueRepository attributeValueRepository) {
                this.categoryRepository = categoryRepository;
                this.productTypeRepository = productTypeRepository;
                this.productRepository = productRepository;
                this.attributeRepository = attributeRepository;
                this.attributeValueRepository = attributeValueRepository;
        }

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                if (this.productRepository.count() > 0) {
                        return;
                }

                System.out.print("Initializing data...");
                // CATEGORIES
                Category manFashionCategory = Category.builder().name("Thời Trang Nam").slug("thoi-trang-nam").build();
                Category manTShirtCategory = Category.builder().name("Áo Thun Nam").slug("ao-thun-nam")
                                .parent(manFashionCategory).build();
                this.categoryRepository.save(manFashionCategory);
                this.categoryRepository.save(manTShirtCategory);

                // ATTRIBUTES
                Attribute colorAttribute = Attribute.builder()
                                .name("Color")
                                .slug("color")
                                .build();
                Attribute sizeAttribute = Attribute.builder()
                                .name("Size")
                                .slug("size")
                                .build();
                this.attributeRepository.saveAll(List.of(colorAttribute, sizeAttribute));

                AttributeValue blackAttribute = AttributeValue.builder()
                                .attribute(colorAttribute)
                                .name("Black")
                                .slug("black")
                                .build();
                AttributeValue whiteAttribute = AttributeValue.builder()
                                .attribute(colorAttribute)
                                .name("White")
                                .slug("white")
                                .build();

                AttributeValue mediumAttribute = AttributeValue.builder()
                                .attribute(sizeAttribute)
                                .name("M")
                                .slug("medium")
                                .build();

                AttributeValue largeAttribute = AttributeValue.builder()
                                .attribute(sizeAttribute)
                                .name("L")
                                .slug("large")
                                .build();

                this.attributeValueRepository
                                .saveAll(List.of(blackAttribute, whiteAttribute, mediumAttribute, largeAttribute));

                // PRODUCT TYPES
                ProductType tShirtType = ProductType.builder().name("Áp Thun").build();
                productTypeRepository.save(tShirtType);

                // PRODUCTS
                Product tShirtOverSize = Product.builder()
                                .name("Áo Thun Oversize Streetwear")
                                .slug("ao-thun-oversize-streetwear")
                                .description("Chất liệu 100% cotton định lượng 250gsm dày dặn, thoáng mát.")
                                .category(manTShirtCategory)
                                .productType(tShirtType)
                                .rating(4.8)
                                .thumbnail("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSG2z0S5MBSGfnbxgPGNcg9iDNVAefXVC6UEtu7K_DJw&s=10")
                                .variants(new ArrayList<>()) // Khởi tạo danh sách variant trống để add vào sau
                                .currency("USD")
                                .build();

                AssignedVariantAttribute blackForVariantM = AssignedVariantAttribute.builder()
                                .attribute(colorAttribute)
                                // .selectedValues(List.of(blackAttribute))
                                .value(blackAttribute)
                                .build();
                AssignedVariantAttribute mediumForVariantM = AssignedVariantAttribute.builder()
                                .attribute(sizeAttribute)
                                .value(mediumAttribute)
                                .build();

                AssignedVariantAttribute whiteForVariantL = AssignedVariantAttribute.builder()
                                .attribute(colorAttribute)
                                .value(whiteAttribute)
                                .build();
                AssignedVariantAttribute largeForVariantL = AssignedVariantAttribute.builder()
                                .attribute(sizeAttribute)
                                .value(largeAttribute)
                                .build();

                // AssignedVariantAttribute sizeAssignedVariantAttributes =
                // AssignedVariantAttribute.builder()
                // .attribute(sizeAttribute)
                // .selectedValues(List.of(mediumAttribute, largeAttribute))
                // .build();

                // PRODUCT VARIANTS
                ProductVariant variantM = ProductVariant.builder()
                                .product(tShirtOverSize)
                                .sku("TSHIRT-OVR-BLK-M")
                                .name("M / Black")
                                .trackInventory(true)
                                .quantity(10)
                                .price(BigDecimal.valueOf(200.0))
                                .assignedAttributes(List.of(blackForVariantM, mediumForVariantM))
                                .build();

                ProductVariant variantL = ProductVariant.builder()
                                .product(tShirtOverSize)
                                .sku("TSHIRT-OVR-BLK-L")
                                .name("L / Black")
                                .trackInventory(true)
                                .quantity(9)
                                .price(BigDecimal.valueOf(190))
                                .assignedAttributes(List.of(whiteForVariantL, largeForVariantL))
                                .build();

                // ADD VARIANT TO PRODUCT AND SAVE (`cascade = CascadeType.ALL`)
                tShirtOverSize.getVariants().addAll(List.of(variantL, variantM));

                tShirtOverSize.setMinPrice(tShirtOverSize
                                .getVariants()
                                .stream()
                                .map(ProductVariant::getPrice)
                                .min(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO));

                tShirtOverSize.setMaxPrice(tShirtOverSize
                                .getVariants()
                                .stream()
                                .map(ProductVariant::getPrice)
                                .max(BigDecimal::compareTo)
                                .orElse(BigDecimal.ZERO));

                blackForVariantM.setVariant(variantM);
                mediumForVariantM.setVariant(variantM);

                whiteForVariantL.setVariant(variantL);
                largeForVariantL.setVariant(variantL);

                this.productRepository.save(tShirtOverSize);

                System.out.print("Initialized data!");
        }
}
