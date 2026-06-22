package com.example.storefront.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.storefront.dto.ProductCreateRequest;
import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.dto.ProductResponse;
import com.example.storefront.entity.AssignedVariantAttribute;
import com.example.storefront.entity.Category;
import com.example.storefront.entity.Product;
import com.example.storefront.entity.ProductVariant;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "productTypeName", source = "productType.name")
    @Mapping(target = "variants", source = "variants")
    ProductDetailResponse toDetailResponse(Product product);

    List<ProductDetailResponse> toDetailResponseList(List<Product> products);

    @Mapping(target = "attributes", source = "assignedAttributes", qualifiedByName = "mapAssignedAttributes")
    ProductDetailResponse.VariantResponse toVariantResponse(ProductVariant variant);

    @Named("mapAssignedAttributes")
    default List<ProductDetailResponse.AttributeValueResponse> mapAssignedAttributes(
            List<AssignedVariantAttribute> assignedAttributes) {
        if (assignedAttributes == null) {
            return new ArrayList<>();
        }

        List<ProductDetailResponse.AttributeValueResponse> result = new ArrayList<>();

        for (AssignedVariantAttribute assigned : assignedAttributes) {
            String attrName = assigned.getAttribute().getName();

            if (assigned.getSelectedValues() != null) {
                for (var value : assigned.getSelectedValues()) {
                    result.add(new ProductDetailResponse.AttributeValueResponse(attrName, value.getName()));
                }
            }
        }
        return result;
    }

    // PRODUCT RESPONSE
    @Mapping(target = "category", source = "category")
    ProductResponse toResponse(Product product);

    ProductResponse.Category toCategoryResponse(Category category);

    List<ProductResponse> toResponseList(List<Product> products);

    // // CREATE PRODUCT

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "productType", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "minPrice", ignore = true)
    @Mapping(target = "maxPrice", ignore = true)
    Product toEntity(ProductCreateRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "assignedAttributes", ignore = true)
    ProductVariant toVariantEntity(ProductCreateRequest.Variant variant);

    List<Product> toEntities(List<ProductCreateRequest> dto);

    // default List<AssignedVariantAttribute> mapCreateAssignedAttributes(
    // List<ProductCreateRequest.Variant.Attribute> attributes) {
    // if (attributes == null)
    // return new ArrayList<>();

    // List<ProductVariant> result = new ArrayList<>();

    // for (var item : attributes) {
    // Attribute attribute = Attribute.builder().
    // }
    // }
}

// Tránh vòng lặp vô tận: Nếu Product có Category, và Category lại có
// List<Product>,
// bạn sẽ bị lỗi StackOverflowError khi map.
// Hãy cẩn thận cấu trúc dữ liệu của bạn hoặc sử dụng @Mapping(target =
// "products", ignore = true) để ngắt vòng lặp.