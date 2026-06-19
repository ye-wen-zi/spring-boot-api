package com.example.storefront.mappers;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.example.storefront.dto.ProductDetailResponse;
import com.example.storefront.entities.AssignedVariantAttribute;
import com.example.storefront.entities.Product;
import com.example.storefront.entities.ProductVariant;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // 1. Map từ gốc: Product Entity -> ProductDetailResponse DTO
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "productTypeName", source = "productType.name")
    @Mapping(target = "variants", source = "variants") // Tự động gọi hàm map list variant bên dưới
    ProductDetailResponse toDetailResponse(Product product);

    // Hàm tiện ích tự động map cả LIST Product sang LIST DTO
    List<ProductDetailResponse> toDetailResponseList(List<Product> products);

    // 2. Map tầng giữa: ProductVariant -> VariantResponse
    @Mapping(target = "attributes", source = "assignedAttributes", qualifiedByName = "mapAssignedAttributes")
    ProductDetailResponse.VariantResponse toVariantResponse(ProductVariant variant);

    // 3. Logic tùy biến (Custom) để làm phẳng cấu trúc EAV lồng nhau của Saleor
    @Named("mapAssignedAttributes")
    default List<ProductDetailResponse.AttributeValueResponse> mapAssignedAttributes(List<AssignedVariantAttribute> assignedAttributes) {
        if (assignedAttributes == null) {
            return new ArrayList<>();
        }
        
        List<ProductDetailResponse.AttributeValueResponse> result = new ArrayList<>();
        
        for (AssignedVariantAttribute assigned : assignedAttributes) {
            String attrName = assigned.getAttribute().getName(); // Lấy tên thuộc tính (ví dụ: "Màu sắc")
            
            if (assigned.getSelectedValues() != null) {
                for (var value : assigned.getSelectedValues()) {
                    // Tạo Record DTO phẳng cho từng cặp [Tên thuộc tính - Giá trị]
                    result.add(new ProductDetailResponse.AttributeValueResponse(attrName, value.getName()));
                }
            }
        }
        return result;
    }
}