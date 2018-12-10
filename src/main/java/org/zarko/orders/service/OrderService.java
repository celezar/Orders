package org.zarko.orders.service;

import java.util.Date;
import java.util.List;
import org.zarko.orders.entity.Order;

public interface OrderService {

    Order createOrder(Order order);

    List<Order> getAllOrdersPlacedBetween(Date from, Date to);

    Order getOrderById(Long id);

    Order updateOrder(Order order);
}
