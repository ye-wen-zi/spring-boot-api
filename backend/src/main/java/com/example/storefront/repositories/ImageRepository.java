package com.example.storefront.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByTargetTypeAndTargetId(String targetType, Long targetId);

    List<Image> findByTargetTypeAndTargetIdIn(String targetType, Collection<Long> targetIds);

    void deleteByTargetTypeAndTargetId(String targetType, Long targetId);
}
