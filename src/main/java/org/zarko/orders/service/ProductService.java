package org.zarko.orders.service;

import java.util.List;
import java.util.Optional;
import org.zarko.orders.entity.Product;

public interface ProductService {

    Product createProduct(Product product);

    List<Product> getAllProducts();

    List<Product> getProductsWithIds(List<Long> productIds);

    Optional<Product> getProductById(Long id);

    Product updateProduct(Product product);
}
