package org.mcs.mcsproductservice.mapper;

import org.mcs.mcsproductservice.dto.FullInfoAboutProductDto;
import org.mcs.mcsproductservice.dto.ProductRequestDto;
import org.mcs.mcsproductservice.dto.ProductResponseDto;
import org.mcs.mcsproductservice.model.Product;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProductMapper {

    public ProductResponseDto mapProductToProductResponseDto(org.mcs.mcsproductservice.model.Product product){
        return ProductResponseDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public org.mcs.mcsproductservice.model.Product mapProductRequestDtoToProduct(ProductRequestDto productRequestDto){
        return org.mcs.mcsproductservice.model.Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .price(productRequestDto.getPrice())
                .createDt(Instant.now())
                .updateDt(Instant.now())
                .build();
    }

    public FullInfoAboutProductDto mapProductToFullInfoAboutProductDto(org.mcs.mcsproductservice.model.Product product){
        return FullInfoAboutProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .createDt(product.getCreateDt())
                .updateDt(product.getUpdateDt())
                .build();
    }

    public Product updateProduct(Product product, ProductRequestDto productRequestDto){
        product.setName(productRequestDto.getName());
        product.setDescription(productRequestDto.getDescription());
        product.setPrice(productRequestDto.getPrice());
        product.setUpdateDt(Instant.now());
        return product;
    }
}
