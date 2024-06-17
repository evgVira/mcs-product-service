package org.mcs.mcsproductservice.controller;

import lombok.RequiredArgsConstructor;
import org.mcs.mcsproductservice.dto.FullInfoAboutProductDto;
import org.mcs.mcsproductservice.dto.ProductRequestDto;
import org.mcs.mcsproductservice.dto.ProductResponseDto;
import org.mcs.mcsproductservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/info/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto getProductById(@PathVariable(name = "productId")Long productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/info/full")
    @ResponseStatus(HttpStatus.OK)
    public FullInfoAboutProductDto getFullInfoAboutProductByName(@RequestParam(name = "productName") String productName){
        return productService.getFullInfoAboutProductByName(productName);
    }

    @PostMapping("/modified/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto productRequestDto){
        return productService.createProduct(productRequestDto);
    }

    @PatchMapping("/modified/update/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDto updateProduct(@PathVariable(name = "productId") Long productId, @RequestBody ProductRequestDto productRequestDto){
        return productService.updateProduct(productId, productRequestDto);
    }

    @DeleteMapping("/modified/delete/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable(name = "productId") Long productId){
        productService.deleteProductById(productId);
    }
}
