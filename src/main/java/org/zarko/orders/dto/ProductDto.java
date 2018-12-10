package org.zarko.orders.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    String name;
    Double price;
}
