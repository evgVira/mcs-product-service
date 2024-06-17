package org.mcs.mcsproductservice.service;

import org.mcs.mcsproductservice.dto.FullInfoAboutProductDto;
import org.mcs.mcsproductservice.dto.ProductRequestDto;
import org.mcs.mcsproductservice.dto.ProductResponseDto;

public interface ProductService {

    ProductResponseDto getProductById(Long productId);
    ProductResponseDto createProduct(ProductRequestDto productRequestDto);

    ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto);

    void deleteProductById(Long id);

    FullInfoAboutProductDto getFullInfoAboutProductByName(String productName);
}

