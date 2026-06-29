package com.example.storefront.services;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.storefront.repositories.CategoryRepository;
import com.example.storefront.utils.SlugUtils;

import lombok.AllArgsConstructor;

import com.example.storefront.dto.CategoryCreateRequest;
import com.example.storefront.entities.Category;
import com.example.storefront.exceptions.ConflictResourceException;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> find() {
        return this.categoryRepository.findAll();
    }

    public Category create(CategoryCreateRequest dto) {
        try {
            Category category = Category.builder()
                    .name(dto.name())
                    .slug(SlugUtils.toSlug(dto.name()))
                    .build();
            return this.categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictResourceException("The category already exist!");
        }
    }

    public void deleteById(UUID id) {
        this.categoryRepository.deleteById(id);
    }
}
