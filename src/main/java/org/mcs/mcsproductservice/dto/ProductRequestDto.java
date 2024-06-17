package org.mcs.mcsproductservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
}
