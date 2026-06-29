package com.example.storefront.entities;

import jakarta.persistence.*;
import lombok.*;

import com.example.storefront.constants.TargetType;

@Entity
@Table(indexes = {
        @Index(name = "idx_images_target", columnList = "target_type, target_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 50)
    private TargetType targetType;

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @Column(name = "url", nullable = false, length = 512)
    private String url;

    @Column(name = "file_type", length = 50)
    private String fileType; // image/png, image/jpeg

    @Column(name = "file_size")
    private Long fileSize;
}