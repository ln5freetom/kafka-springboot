package com.example.ecommerce.mapper;

import com.example.ecommerce.dto.ProductRequest;
import com.example.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequest request);
    void updateEntity(ProductRequest request, @MappingTarget Product product);
}
