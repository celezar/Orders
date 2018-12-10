package org.zarko.orders.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {

    private String buyerEmail;
    private List<OrderItemDto> items;
    @ApiModelProperty(hidden = true)
    private Date placedAt;
    @ApiModelProperty(hidden = true)
    private Double total;
}
