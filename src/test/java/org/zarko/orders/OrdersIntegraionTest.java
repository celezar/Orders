package org.zarko.orders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import org.zarko.orders.dto.OrderDto;
import org.zarko.orders.dto.OrderItemDto;
import org.zarko.orders.dto.ProductDto;
import org.zarko.orders.entity.Order;
import org.zarko.orders.entity.Product;
import org.zarko.orders.repository.OrdersRepository;
import org.zarko.orders.repository.ProductRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("h2")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdersIntegraionTest {

    public static final String PRODUCT_NAME = "test product";

    public static final Double PRODUCT_PRICE = 2.4;

    public static final Double QUANTITY = 2.0;

    public static final String NEW_PRODUCT_NAME = "new name";

    public static final String BUYER_EMAIL = "test@email.com";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Test
    public void test1AddNewProduct() throws Exception {
        ProductDto productDto = ProductDto.builder()
                .name(PRODUCT_NAME)
                .price(PRODUCT_PRICE)
                .build();

        ResponseEntity<Long> response = restTemplate.postForEntity("/products", productDto, Long.class);

        assertEquals("New product id was not returned", 1L, (long)response.getBody());
        Optional<Product> productOptional = productRepository.findById(1L);
        assertTrue(productOptional.isPresent());
        assertEquals("Product name is not set.", PRODUCT_NAME, productOptional.get().getProductName());
        assertEquals("Product price is not set.", PRODUCT_PRICE, productOptional.get().getPrice());
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

        Optional<Product> productOptional = productRepository.findById(1L);
        assertTrue(productOptional.isPresent());
        assertEquals("Product name is not updated", NEW_PRODUCT_NAME, productOptional.get().getProductName());
    }

    @Test
    public void test4CreateOrder() throws Exception {
        List<OrderItemDto> orderItems = Arrays.asList(OrderItemDto.builder()
                .productId(1L)
                .quantity(2.0)
                .build());
        OrderDto orderDto = OrderDto.builder()
                .buyerEmail(BUYER_EMAIL)
                .items(orderItems)
                .build();

        ResponseEntity<Long> response = restTemplate.postForEntity("/orders", orderDto, Long.class);

        assertEquals("New order id was not returned", 1L, (long)response.getBody());
        Optional<Order> orderOptional = ordersRepository.findById(1L);
        assertTrue(orderOptional.isPresent());
        assertEquals("Buyer's email is not set.", BUYER_EMAIL, orderOptional.get().getBuyerEmail());
        assertEquals("Order total is not correct.", PRODUCT_PRICE * QUANTITY,
                orderOptional.get().getTotal(), 0.01);
        assertEquals("Number of order items is not correct.", 1, orderOptional.get().getOrderItems().size());
    }

    @Test
    public void test5GetOrdersByDate() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Date from = Date.from(new Date().toInstant().minus(2, ChronoUnit.MINUTES));
        Date to = Date.from(new Date().toInstant().plus(2, ChronoUnit.MINUTES));
        Map<String, String> params = new HashMap<>();
        params.put("from", sdf.format(from));
        params.put("to", sdf.format(to));
        List<LinkedHashMap> orders = restTemplate.getForObject("/orders/between?from={from}&to={to}", List.class, params);

        assertEquals("Wrong number of orders found", 1, orders.size());
        assertEquals("Email is not correct", BUYER_EMAIL, orders.get(0).get("buyerEmail"));
    }

    @Test
    public void test6GetOrder() throws Exception {
        LinkedHashMap order = restTemplate.getForObject("/orders/1", LinkedHashMap.class, Collections.emptyMap());

        assertEquals("Email is not correct", BUYER_EMAIL, order.get("buyerEmail"));
        assertEquals("Total is not correct", QUANTITY * PRODUCT_PRICE, order.get("total"));
    }

    @Test
    public void test7RecalculateOrderTotal() throws Exception {
        Product product = Product.builder()
                .id(1L)
                .price(PRODUCT_PRICE * 2)
                .productName(PRODUCT_NAME)
                .build();
        productRepository.save(product);

        Double total = restTemplate.getForObject("/orders/recalculate/1", Double.class, Collections.emptyMap());

        assertEquals("Total does not match", QUANTITY * PRODUCT_PRICE * 2, total, 0.01);
    }
}
