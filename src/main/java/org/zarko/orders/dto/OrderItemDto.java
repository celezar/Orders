package org.zarko.orders.dto;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long productId;
    private Double quantity;
    @ApiModelProperty(hidden = true)
    private Double price;
    @ApiModelProperty(hidden = true)
    private String productName;
}
