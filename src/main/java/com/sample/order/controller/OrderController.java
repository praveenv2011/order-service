    package com.sample.order.controller;

    import com.sample.order.dto.OrderDTO;
    import com.sample.order.exception.ApplicationException;
    import com.sample.order.service.OrderService;
    import org.modelmapper.ModelMapper;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

    import java.net.URI;
    import java.util.List;

    @RestController
    @RequestMapping("/api/")
    public class OrderController {

        private final OrderService orderService;

        private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

        public OrderController(OrderService orderService) {
            this.orderService = orderService;
        }

        @PostMapping("users/{userid}/addresses/{addressid}/orders")
        public ResponseEntity<OrderDTO> placeOrder(@PathVariable("userid") Long userId,
                                                   @PathVariable("addressid") Long addressId,
                                                   @RequestParam("paymentmode") String paymentMode) throws ApplicationException {
            logger.info("Placing order for userId: {}, addressId: {}, paymentMode: {}", userId, addressId, paymentMode);
            OrderDTO orderStatus = orderService.placeOrder(userId,addressId,paymentMode);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                            .path("/{orderId}")
                            .buildAndExpand(orderStatus.getOrderId())
                                    .toUri();
            logger.info("Order placed successfully with status: {}", orderStatus);
            return ResponseEntity.created(location).body(orderStatus);
        }

        @GetMapping("orders/{userId}")
        public ResponseEntity<List<OrderDTO>> getOrdersByUserId(@PathVariable("userId") Long userId) throws ApplicationException {
            logger.info("Requesting orders for userId: {}", userId);
            List<OrderDTO> orders = orderService.getByUserId(userId);
            logger.info("Orders fetched successfully for userId: {}, total orders: {}", userId, orders.size());
            return ResponseEntity.ok().body(orders);
        }

        @GetMapping("orders/search")
        public ResponseEntity<OrderDTO> getOrderByUserIdAndOrderId(@RequestParam("userId") Long userId,
                                                                   @RequestParam("orderId") Long orderId) throws ApplicationException {
            logger.info("Requesting order for userId: {}, orderId: {}", userId, orderId);
            OrderDTO order = orderService.getByUserIdAndOrderId(userId,orderId);
            logger.info("Order fetched successfully for userId: {}, orderId: {}", userId, orderId);
            return ResponseEntity.ok().body(order);
        }

    }
