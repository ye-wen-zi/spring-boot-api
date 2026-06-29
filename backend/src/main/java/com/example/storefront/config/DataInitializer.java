package com.example.storefront.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.storefront.constants.Role;
import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.entities.User;
import com.example.storefront.repositories.ProductRepository;
import com.example.storefront.repositories.UserRepository;
import com.example.storefront.services.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

// @Component
public class DataInitializer implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final ProductService productService;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public DataInitializer(
                        ProductRepository productRepository,
                        ProductService productService,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
                this.productRepository = productRepository;
                this.productService = productService;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                if (this.productRepository.count() == 0) {
                        System.out.print("Initializing data...");
                        TypeReference<List<ProductCreateRequest>> typeReference = new TypeReference<List<ProductCreateRequest>>() {
                        };

                        InputStream inputStream = TypeReference.class.getResourceAsStream("/data/products.json");
                        List<ProductCreateRequest> productReqList = new ObjectMapper().readValue(inputStream,
                                        typeReference);
                        productReqList.forEach(this.productService::save);
                        // System.out.print(productReqList);
                        System.out.print("Initialized data!");
                }

                if (this.userRepository.count() == 0) {
                        var user = User.builder()
                                        .firstName("Wen Zi")
                                        .lastName("Ye")
                                        .email("user123@example.com")
                                        .password(this.passwordEncoder.encode("123456789"))
                                        .build();

                        var admin = User.builder()
                                        .firstName("Admin")
                                        .lastName("Ye")
                                        .email("admin123@example.com")
                                        .password(this.passwordEncoder.encode("123456789"))
                                        .role(Role.ADMIN)
                                        .build();
                        this.userRepository.saveAll(List.of(user, admin));
                }
        }
}
