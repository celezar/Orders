package org.zarko.orders.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.websocket.server.PathParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zarko.orders.dto.OrderDto;
import org.zarko.orders.dto.OrderItemDto;
import org.zarko.orders.entity.Order;
import org.zarko.orders.entity.OrderItem;
import org.zarko.orders.entity.OrderProductId;
import org.zarko.orders.entity.Product;
import org.zarko.orders.service.OrderService;
import org.zarko.orders.service.ProductService;

@RestController
@RequestMapping("orders")
@Api(value = "Order controller", description = "Operation pertaining to orders")
public class OrderController {

    public static final String JSON = MimeTypeUtils.APPLICATION_JSON_VALUE;

    private OrderService orderService;

    private ProductService productService;

    public OrderController(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;
    }

    @ApiOperation(value = "Creates new order", response = Long.class)
    @PostMapping(consumes = JSON, produces = JSON)
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto) {
        Order order = createOrderFromDto(orderDto);
        order = orderService.createOrder(order);
        return ResponseEntity.ok(order.getId());
    }

    @ApiOperation(value = "Retrieves all orders placed between dates", response = OrderDto.class)
    @GetMapping("between")
    public List<OrderDto> getOrdersByDate(@RequestParam("from")
                                          @ApiParam(name = "From date", value = "Date in ISO date time format")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date from,
                                          @RequestParam("to")
                                          @ApiParam(name = "To date", value = "Date in ISO date time format")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date to) {
        return orderService.getAllOrdersPlacedBetween(from, to).stream()
                .map(order -> createOrderDto(order))
                .collect(Collectors.toList());
    }

    @ApiOperation(value = "Finds order by specified id", response = OrderDto.class)
    @GetMapping(value = "/{id}", produces = JSON)
    public OrderDto getOrder(@ApiParam(value = "Id of the order", required = true, type = "Long")
                              @PathVariable("id") long id) {
        Order order = orderService.getOrderById(id);
        return createOrderDto(order);
    }

    @ApiOperation(value = "Recalculates and returns total price of order", response = Double.class)
    @GetMapping(value = "/recalculate/{id}", produces = JSON)
    public Double recalculateOrderTotal(@ApiParam(value = "Id of the order", required = true, type = "Long")
                             @PathVariable("id") long id) {
        Order order = orderService.getOrderById(id);
        for(OrderItem item: order.getOrderItems()) {
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
        }
        order = orderService.updateOrder(order);
        return order.getOrderItems().stream().mapToDouble(value -> value.getPrice()).sum();
    }

    private OrderDto createOrderDto(Order order) {
        List<OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                .map(orderItem -> OrderItemDto.builder()
                        .price(orderItem.getPrice())
                        .productId(orderItem.getProduct().getId())
                        .productName(orderItem.getProduct().getProductName())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList());
        return OrderDto.builder()
                .placedAt(order.getPlacedAt())
                .buyerEmail(order.getBuyerEmail())
                .items(orderItemDtos)
                .total(order.getTotal())
                .build();
    }

    private Order createOrderFromDto(OrderDto orderDto) {
        Order order = Order.builder()
                .buyerEmail(orderDto.getBuyerEmail())
                .placedAt(new Date())
                .build();
        List<OrderItem> orderItems = new ArrayList<>();
        if (orderDto.getItems() != null) {
            for (OrderItemDto item : orderDto.getItems()) {
                Optional<Product> productOptional = productService.getProductById(item.getProductId());
                Product product = productOptional.orElseThrow(() -> new NoSuchElementException());
                orderItems.add(OrderItem.builder()
                        .product(product)
                        .quantity(item.getQuantity())
                        .price(item.getQuantity() * product.getPrice())
                        .order(order)
                        .build());
            }
        }
        order.setOrderItems(orderItems);
        return order;
    }
}
