package org.zarko.orders.api;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.zarko.orders.dto.OrderDto;
import org.zarko.orders.entity.Order;
import org.zarko.orders.entity.OrderItem;
import org.zarko.orders.entity.Product;
import org.zarko.orders.service.OrderService;
import org.zarko.orders.service.ProductService;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    public static final String BUYER_EMAIL = "test@email.com";

    public static final Double TOTAL = 1.0;

    public static final Double NEW_PRICE = 2.0;

    public static final Double QUANTITY = 2.0;

    public static final Long ORDER_ID = 1L;

    @Mock
    OrderService orderService;

    @Mock
    ProductService productService;

    @InjectMocks
    OrderController orderController;

    @Test
    public void testNewOrderCreation() throws Exception {
        OrderDto orderDto = OrderDto.builder().buyerEmail(BUYER_EMAIL).total(TOTAL).build();
        Order order = Order.builder().id(ORDER_ID).buyerEmail(BUYER_EMAIL).total(TOTAL).build();
        when(orderService.createOrder(any())).thenReturn(order);

        ResponseEntity<Long> responseEntity = orderController.createOrder(orderDto);

        assertTrue(responseEntity.getStatusCode() == HttpStatus.OK);
        assertTrue(responseEntity.getBody() == ORDER_ID);

    }

    @Test
    public void testRecalculateOrderTotal() throws Exception {
        Order order = Order.builder().id(1L).buyerEmail(BUYER_EMAIL).total(TOTAL).build();
        List<OrderItem> orderItems = Arrays.asList(OrderItem.builder()
                .product(Product.builder().price(NEW_PRICE).build())
                .quantity(QUANTITY).build());
        order.setOrderItems(orderItems);
        when(orderService.getOrderById(ORDER_ID)).thenReturn(order);
        when(orderService.updateOrder(order)).thenReturn(order);

        Double total = orderController.recalculateOrderTotal(ORDER_ID);

        assertTrue(total == 4.0);

    }
}
