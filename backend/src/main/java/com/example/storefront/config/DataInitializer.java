package com.example.storefront.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.storefront.constants.Role;
import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.entities.Attribute;
import com.example.storefront.entities.Category;
import com.example.storefront.entities.ProductType;
import com.example.storefront.entities.User;
import com.example.storefront.exceptions.ResourceNotFoundException;
import com.example.storefront.repositories.AttributeRepository;
import com.example.storefront.repositories.CategoryRepository;
import com.example.storefront.repositories.ProductTypeRepository;
import com.example.storefront.repositories.UserRepository;
import com.example.storefront.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed-data", havingValue = "true", matchIfMissing = false)
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductTypeRepository productTypeRepository;
    private final AttributeRepository attributeRepository;
    private final ProductService productService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Initializing categories...");
        this._initCatategories();

        System.out.println("Initializing product's types...");
        this._initProductTypes();

        System.out.println("Initializing product's attributes...");
        this._initAttributes();

        System.out.println("Initializing attribute's values...");
        this._initAttributeValues();

        System.out.println("Initializing products...");
        this._initProducts();

        System.out.println("Initializing users...");
        this._initUsers();

        System.out.println("Initialized data!");
    }

    private <T> List<T> loadJsonData(String path, Class<T> clazz) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(path);
        var collectionType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
        return objectMapper.readValue(inputStream, collectionType);
    }

    private void _initProducts() {
        var category = this.categoryRepository.findFirstBy()
                .orElseThrow(() -> new ResourceNotFoundException("There are no categories."));
        var type = this.productTypeRepository.findFirstBy()
                .orElseThrow(() -> new ResourceNotFoundException("There are no product's type."));

        var attributes = this.attributeRepository.findAll();
        List<ProductCreateRequest> productReqList;
        Random random = new Random();
        try {
            productReqList = this.loadJsonData("/data/products.json", ProductCreateRequest.class);
            productReqList.stream().map(item -> item.toBuilder()
                    .categoryId(category.getId())
                    .typeId(type.getId())
                    .images(List.of(
                            "https://example.com/ao-thun-1.jpg",
                            "https://example.com/ao-thun-2.jpg",
                            "https://example.com/ao-thun-3.jpg",
                            "https://example.com/ao-thun-4.jpg",
                            "https://example.com/ao-thun-5.jpg"))
                    .variants(item.variants().stream().map(v -> v.toBuilder()
                            .attributes(v.attributes().stream().map(
                                    attr -> {
                                        final int attributeIndex = random
                                                .nextInt(attributes
                                                        .size());
                                        return attr.toBuilder()
                                                .attributeId(attributes
                                                        .get(attributeIndex)
                                                        .getId())
                                                .attributeValueId(
                                                        attributes.get(attributeIndex)
                                                                .getValues()
                                                                .get(0)
                                                                .getId())
                                                .build();
                                    })
                                    .toList())
                            .build())
                            .toList())
                    .build()).forEach(this.productService::save);
            ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    private void _initAttributes() throws IOException {
        var attributes = this.loadJsonData("/data/attributes.json", Attribute.class);
        var sql = this.createSql("INSERT INTO attribute (id, name, slug) VALUES",
                List.of("id", "name", "slug"),
                attributes.size());

        var command = this.entityManager.createNativeQuery(sql);
        for (var i = 0; i < attributes.size(); i++) {
            var attr = attributes.get(i);
            command.setParameter("id" + i, attr.getId());
            command.setParameter("name" + i, attr.getName());
            command.setParameter("slug" + i, attr.getSlug());
        }
        command.executeUpdate();
    }

    @Transactional
    private void _initAttributeValues() throws IOException {

        record Value(
                UUID id, String name, String slug, UUID attributeId) {
        }

        List<Value> values = new ArrayList<>();

        this.loadJsonData("/data/attributes.json", Attribute.class)
                .forEach(attr -> {
                    attr.getValues()
                            .forEach(v -> values.add(new Value(
                                    v.getId(),
                                    v.getName(),
                                    v.getSlug(),
                                    attr.getId())));
                });

        var sql = this.createSql("INSERT INTO attribute_value (id, name, slug, attribute_id) VALUES",
                List.of("id", "name", "slug", "attribute_id"),
                values.size());

        var command = this.entityManager.createNativeQuery(sql);
        for (var i = 0; i < values.size(); i++) {
            var value = values.get(i);
            command.setParameter("id" + i, value.id());
            command.setParameter("name" + i, value.name());
            command.setParameter("slug" + i, value.slug());
            command.setParameter("attribute_id" + i, value.attributeId());
        }
        command.executeUpdate();
    }

    @Transactional
    private void _initProductTypes() throws IOException {
        var productTypes = this.loadJsonData("/data/product_types.json", ProductType.class);
        var sql = this.createSql("INSERT INTO product_type (id, name, has_variants, is_shipping_required) VALUES",
                List.of("id", "name", "has_variants", "is_shipping_required"),
                productTypes.size());

        var command = this.entityManager.createNativeQuery(sql);
        for (var i = 0; i < productTypes.size(); i++) {
            var type = productTypes.get(i);
            command.setParameter("id" + i, type.getId());
            command.setParameter("name" + i, type.getName());
            command.setParameter("has_variants" + i, true);
            command.setParameter("is_shipping_required" + i, true);
        }
        command.executeUpdate();
    }

    @Transactional
    private void _initCatategories() throws IOException {
        var categories = this.loadJsonData("/data/categories.json", Category.class);

        var sql = this.createSql("INSERT INTO category (id, name, slug) VALUES", List.of("id", "name", "slug"),
                categories.size());

        var command = this.entityManager.createNativeQuery(sql);

        for (var i = 0; i < categories.size(); i++) {
            var category = categories.get(i);
            command.setParameter("id" + i, category.getId());
            command.setParameter("name" + i, category.getName());
            command.setParameter("slug" + i, category.getSlug());
        }

        command.executeUpdate();
    }

    private List<User> _initUsers() {
        var user = User.builder()
                .email("user@example.com")
                .firstName("User")
                .lastName("Seeding")
                .password(this.passwordEncoder.encode("A0123456789$"))
                .build();

        var admin = User.builder()
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("Seeding")
                .password(this.passwordEncoder.encode("A0123456789$"))
                .role(Role.ADMIN)
                .build();
        return this.userRepository.saveAll(List.of(user, admin));
    }

    private String createSql(String prefix, List<String> columns, int rowCount) {
        StringBuilder builder = new StringBuilder();

        builder.append(prefix).append(" ");

        String valuesSql = IntStream.range(0, rowCount)
                .mapToObj(i -> columns.stream()
                        .map(col -> ":" + col.strip() + i)
                        .collect(Collectors.joining(", ", "(", ")")))
                .collect(Collectors.joining(", "));
        builder.append(valuesSql);
        return builder.toString();
    }
}
