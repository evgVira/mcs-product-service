package org.mcs.mcsproductservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FullInfoAboutProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Instant createDt;
    private Instant updateDt;
}
