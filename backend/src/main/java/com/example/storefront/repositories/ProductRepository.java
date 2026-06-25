package com.example.storefront.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.storefront.entities.Product;

@Repository()
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productType")
    List<Product> findAllWithProductType();

}
