package com.example.storefront.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.storefront.constants.TargetType;
import com.example.storefront.entities.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByTargetTypeAndTargetId(TargetType targetType, Long targetId);

    List<Image> findByTargetTypeAndTargetIdIn(TargetType targetType, Collection<Long> targetIds);

    void deleteByTargetTypeAndTargetId(TargetType targetType, Long targetId);
}
