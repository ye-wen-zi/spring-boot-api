package com.example.storefront.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.storefront.dto.AttributeCreateRequest;
import com.example.storefront.entities.Attribute;
import com.example.storefront.entities.AttributeValue;
import com.example.storefront.exceptions.ResourceNotFoundException;
import com.example.storefront.repositories.AttributeRepository;
import com.example.storefront.repositories.AttributeValueRepository;
import com.example.storefront.utils.SlugUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final AttributeValueRepository attributeValueRepository;

    @Transactional(readOnly = true)
    public List<Attribute> find() {
        return this.attributeRepository.findWithValues();
    }

    public Attribute create(AttributeCreateRequest dto) {
        var attribute = Attribute.builder().name(dto.name())
                .slug(SlugUtils.toSlug(dto.name()))
                .build();

        var attributeValues = dto.values().stream().map(v -> AttributeValue.builder()
                .attribute(attribute)
                .name(v)
                .slug(SlugUtils.toSlug(v))
                .build()).toList();

        attribute.setValues(attributeValues);

        return this.attributeRepository.save(attribute);
    }

    public void delete(UUID id) {
        this.attributeRepository.deleteById(id);
    }

    public List<AttributeValue> addValues(List<String> values, UUID attributeId) {
        var attribute = this.attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException("The attibute does not exits!"));

        var attributeValues = values.stream().map(v -> AttributeValue.builder()
                .attribute(attribute)
                .name(v)
                .slug(SlugUtils.toSlug(v))
                .build()).toList();

        return this.attributeValueRepository.saveAll(attributeValues);
    }

    public void deleteValuesByAttributeValueIds(List<UUID> ids) {
        this.attributeValueRepository.deleteAllById(ids);
    }
}
