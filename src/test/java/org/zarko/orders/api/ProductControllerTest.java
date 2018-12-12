package org.zarko.orders.api;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.zarko.orders.dto.ProductDto;
import org.zarko.orders.entity.Product;
import org.zarko.orders.service.ProductService;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    public static final String PRODUCT_NAME = "test product";

    public static final Double PRODUCT_PRICE = 2.4;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductController productController;

    @Test
    public void testNewProductCreation() throws Exception {
        ProductDto productDto = ProductDto.builder().name(PRODUCT_NAME).price(PRODUCT_PRICE).build();
        Product product = Product.builder().id(1L).productName(PRODUCT_NAME).price(PRODUCT_PRICE).build();
        when(productService.createProduct(any())).thenReturn(product);

        ResponseEntity<Long> responseEntity = productController.createProduct(productDto);

        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
        assertTrue(responseEntity.getBody() == 1L);
    }
}
