package org.zarko.orders.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.zarko.orders.entity.Product;
import org.zarko.orders.repository.ProductRepository;

@Service
public class DefaultProductService implements ProductService {

    private ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsWithIds(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
}
