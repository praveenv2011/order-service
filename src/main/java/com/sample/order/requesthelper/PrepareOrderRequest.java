package com.sample.order.requesthelper;

import com.sample.order.dto.*;
import com.sample.order.exception.OrderAccessException;
import com.sample.order.exception.OrderItemAccessException;
import com.sample.order.httprequest.HttpRequest;
import com.sample.order.model.Order;
import com.sample.order.model.OrderItem;
import com.sample.order.repository.OrderItemRepository;
import com.sample.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrepareOrderRequest {

    private final HttpRequest request;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(PrepareOrderRequest.class);

    public PrepareOrderRequest(HttpRequest request, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.request = request;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderDTO prepareRequest(UserDTO user, AddressDTO address, CartDTO cart, String paymentMethod){
        logger.info("Preparing order request for user: {}, address: {}, cart: {}, paymentMethod: {}", user, address, cart, paymentMethod);
        if(cart.getCartItems().isEmpty()){
            return null;
        }

        logger.info("Creating new order for user: {}, address: {}, cart: {}, paymentMethod: {}", user, address, cart, paymentMethod);
        Order order = new Order();

        order.setOrderDate(java.time.LocalDateTime.now());
        order.setOrderStatus("pending");
        order.setTotalAmount(cart.getTotalAmount());
        order.setShippingAddress(address.getHouseNo() + "," +
                address.getBuildingName() + "," +
                address.getArea() + "," +
                address.getCity() + "," +
                address.getState() + "-" + address.getPincode() + "," +
                "phone number"+ "-" + user.getPhoneNumber());
        order.setUserId(user.getUserId());
        order.setPaymentMode(paymentMethod);
        order.setPhoneNumber(user.getPhoneNumber());

        logger.info("Saving order to repository: {}", order);

        try {
            orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", order.getOrderId());
        }catch (DataAccessResourceFailureException e){
            throw new OrderAccessException("Failed to save order due to database access issue", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItemDTO cartItemDTO : cart.getCartItems()){

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setPrice(cartItemDTO.getPrice());
            orderItem.setQuantity(cartItemDTO.getQuantity());
            orderItem.setProductId(cartItemDTO.getProductId());

            orderItems.add(orderItem);

            logger.info("Saving order item to repository: {}", orderItem);
            try {
                orderItemRepository.save(orderItem);
                logger.info("Order items saved successfully, updating order with items: {}", orderItems);
            }catch (DataAccessResourceFailureException e){
                throw new OrderItemAccessException("Failed to save order item due to database access issue", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        order.setOrderItems(orderItems);

        try {
            orderRepository.save(order);
            logger.info("Order saved successfully with ID: {}", order.getOrderId());
        }catch (DataAccessResourceFailureException e){
            throw new OrderAccessException("Failed to save order due to database access issue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("Order placed successfully with ID: {}", order.getOrderId());

        return new ModelMapper().map(order, OrderDTO.class);
    }
}
