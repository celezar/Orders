package org.zarko.orders.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zarko.orders.entity.Order;

public interface OrdersRepository extends JpaRepository<Order, Long> {

    @Query(value = "select sum(PRICE) from order_item where order_id = ?1", nativeQuery = true)
    Double calculateOrderSum(Long orderId);

    List<Order> findByPlacedAtBetween(Date from, Date to);
}
