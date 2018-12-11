package org.zarko.orders;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.zarko.orders.dto.ProductDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdersIntegraionTest {

    public static final String PRODUCT_NAME = "test product";

    public static final Double PRODUCT_PRICE = 2.4;

    public static final String NEW_PRODUCT_NAME = "new name";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void test1AddNewProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build();

        ResponseEntity<Long> response = restTemplate.postForEntity("/products", productDto, Long.class);

        assertEquals("New product was not created", 1L, (long)response.getBody());
    }

    @Test
    public void test2GetProductList() throws Exception {
        List<LinkedHashMap> productList = restTemplate.getForObject("/products", List.class, Collections.emptyMap());

        assertEquals("Wrong number of products.", productList.size(), 1);
        assertEquals("Product name is wrong", PRODUCT_NAME, productList.get(0).get("productName"));
        assertEquals("Product price is wrong", PRODUCT_PRICE, productList.get(0).get("price"));
    }

    @Test
    public void test3UpdateProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name(NEW_PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build();

        restTemplate.put("/products/1", productDto, Collections.emptyMap());
        List<LinkedHashMap> productList = restTemplate.getForObject("/products", List.class, Collections.emptyMap());

        assertEquals("Product name is not updated", NEW_PRODUCT_NAME, productList.get(0).get("productName"));
    }
}
