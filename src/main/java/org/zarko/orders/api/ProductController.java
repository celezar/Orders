package org.zarko.orders.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zarko.orders.dto.ProductDto;
import org.zarko.orders.entity.Product;
import org.zarko.orders.service.ProductService;

@RestController
@RequestMapping("products")
@Api(value = "Products controller", description = "Operation pertaining to products.")
public class ProductController {

    public static final String JSON = MimeTypeUtils.APPLICATION_JSON_VALUE;

    private ProductService productService;

    public ProductController (ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Creates new product", response = Long.class)
    @PostMapping(consumes = JSON, produces = JSON)
    public ResponseEntity<Long> createProduct(@RequestBody ProductDto productDto) {
        Product product = createProductFromDto(productDto);
        product = productService.createProduct(product);
        return ResponseEntity.ok(product.getId());
    }

    @ApiOperation(value = "Retrieves list of all products", response = Product.class)
    @GetMapping(produces = JSON)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @ApiOperation(value = "Updates product", response = Product.class)
    @PutMapping(value = "{id}", produces = JSON)
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Product product = createProductFromDto(productDto);
        product.setId(id);
        return productService.updateProduct(product);
    }

    private Product createProductFromDto(ProductDto productDto) {
        return Product.builder()
                .productName(productDto.getName())
                .price(productDto.getPrice())
                .build();
    }
}
