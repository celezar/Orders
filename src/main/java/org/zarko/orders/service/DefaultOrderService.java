package org.zarko.orders.service;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;
import org.zarko.orders.entity.Order;
import org.zarko.orders.repository.OrdersRepository;

@Service
public class DefaultOrderService implements OrderService {

    private OrdersRepository ordersRepository;

    public DefaultOrderService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }


    @Override
    public Order createOrder(Order order) {
        return ordersRepository.save(order);
    }

    @Override
    public List<Order> getAllOrdersPlacedBetween(Date from, Date to) {
        return ordersRepository.findByPlacedAtBetween(from, to);
    }

    @Override
    public Order getOrderById(Long id) {
        return ordersRepository.getOne(id);
    }

    @Override
    public Order updateOrder(Order order) {
        return ordersRepository.save(order);
    }
}
