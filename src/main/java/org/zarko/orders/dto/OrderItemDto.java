package org.zarko.orders.dto;

import io.swagger.annotations.ApiModelProperty;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {

    private Long productId;
    private Double quantity;
    @ApiModelProperty(hidden = true)
    private Double price;
    @ApiModelProperty(hidden = true)
    private String productName;
}
