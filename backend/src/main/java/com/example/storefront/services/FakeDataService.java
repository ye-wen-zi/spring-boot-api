package com.example.storefront.services;

import com.example.storefront.dto.AuthSignupRequest;
import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductUpdateRequest;
import com.example.storefront.entities.Product;
import com.example.storefront.entities.ProductVariant;
import com.example.storefront.mappers.ProductMapper;
import com.example.storefront.repositories.AssignedAttributeRepository;
import com.example.storefront.repositories.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class FakeDataService {

    private final Faker faker;
    private final ProductRepository productRepository;
    private final AssignedAttributeRepository assignedAttributeRepository;
    private final ProductMapper productMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    private Product _createProduct() {
        var product = this.productRepository.findFirstWithCategoryAndTypeAndVariantsBy().orElseThrow();

        this.entityManager.detach(product);
        product.getVariants().forEach(this.entityManager::detach);

        var variantIdS = product.getVariants().stream().map(ProductVariant::getId).toList();
        var assignedAttributesMap = this.assignedAttributeRepository
                .findWithAttributeAndValueByVariant_IdIn(variantIdS)
                .stream().collect(Collectors.groupingBy(assigned -> assigned.getVariant().getId()));

        product.getVariants().forEach(
                v -> v.setAssignedAttributes(assignedAttributesMap.getOrDefault(v.getId(), List.of())));

        return product;
    }

    @Transactional(readOnly = true)
    public ProductCreateRequest generateFakeProductRequest() {

        var product = this._createProduct();

        List<String> fakeImages = IntStream.range(0, faker.number().numberBetween(3,
                8))
                .mapToObj(i -> faker.internet().image(800, 600, "product"))
                .collect(Collectors.toList());

        product.getVariants().forEach(v -> {
            v.setName(faker.color().name() + " - " + faker.commerce().material());
            v.setPrice(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)));
            v.setQuantity(faker.number().numberBetween(0, 500));
            v.setSku(faker.commerce().promotionCode(8).toUpperCase());
            v.setTrackInventory(faker.bool().bool());
        });

        String[] currencies = { "USD", "EUR", "JPY", "GBP", "AUD", "CAD", "CHF", "CNY", "HKD", "SGD", "KRW",
                "VND" };

        return this.productMapper.toCreateRequest(product).toBuilder()
                .name(faker.commerce().productName())
                .currency(faker.options().option(currencies))
                .description(faker.lorem().maxLengthSentence(150))
                .images(fakeImages)
                .thumbnail(faker.internet().image(400, 400, "thumbnail"))
                .build();
    }

    public AuthSignupRequest generateAuthSignupData() {
        return new AuthSignupRequest(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.regexify("[A-Z]{2}[a-z]{3}[0-9]{2}[@$!%*?&]{3}"));
    }

    @Transactional(readOnly = true)
    public ProductUpdateRequest generateProductUpdateRequest() {
        var product = this._createProduct();

        List<String> fakeImages = IntStream.range(0, faker.number().numberBetween(3,
                8))
                .mapToObj(i -> faker.internet().image(800, 600, "product"))
                .collect(Collectors.toList());

        return this.productMapper.toUpdateRequest(product).toBuilder()
                .images(fakeImages)
                .build();
    }
}