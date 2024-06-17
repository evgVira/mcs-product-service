package org.mcs.mcsproductservice.service;

import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.dto.FullInfoAboutProductDto;
import org.mcs.mcsproductservice.dto.ProductRequestDto;
import org.mcs.mcsproductservice.dto.ProductResponseDto;
import org.mcs.mcsproductservice.mapper.ProductMapper;
import org.mcs.mcsproductservice.model.Product;
import org.mcs.mcsproductservice.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public ProductResponseDto getProductById(Long productId){
        org.mcs.mcsproductservice.model.Product product = getProduct(productId);
        ProductResponseDto productResponseDto = productMapper.mapProductToProductResponseDto(product);
        return productResponseDto;
    }

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto){
        org.mcs.mcsproductservice.model.Product product = productMapper.mapProductRequestDtoToProduct(productRequestDto);
        productRepository.save(product);
        ProductResponseDto productResponseDto = productMapper.mapProductToProductResponseDto(product);
        return productResponseDto;
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto productRequestDto){
        Product product = getProduct(productId);
        Product updateProduct = productMapper.updateProduct(product, productRequestDto);
        productRepository.save(updateProduct);
        ProductResponseDto productResponseDto = productMapper.mapProductToProductResponseDto(updateProduct);
        return productResponseDto;
    }

    @Override
    @Transactional
    public void deleteProductById(Long id){
        Product product = getProduct(id);
        productRepository.delete(product);
    }

    @Override
    public FullInfoAboutProductDto getFullInfoAboutProductByName(String productName){
        Product product = productRepository.findProductByName(productName);
        FullInfoAboutProductDto fullInfoAboutProductDto = productMapper.mapProductToFullInfoAboutProductDto(product);
        return fullInfoAboutProductDto;
    }

    private Product getProduct(Long id){
        org.mcs.mcsproductservice.model.Product product = productRepository.findProductById(id);
        return product;
    }

}
