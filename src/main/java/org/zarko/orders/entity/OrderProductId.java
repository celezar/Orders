package org.zarko.orders.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderProductId implements Serializable {

    private Product product;

    private Order order;
}
