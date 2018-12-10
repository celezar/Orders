package org.zarko.orders.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderProductId.class)
public class OrderItem {

    @Id
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Id
    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    @JsonIgnore
    private Order order;

    private Double price;

    private Double quantity;
}
