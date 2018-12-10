package org.zarko.orders.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zarko.orders.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByIdIn(List<Long> productIds);
}
