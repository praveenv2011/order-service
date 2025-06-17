package com.sample.order.service;

import com.sample.order.dto.*;
import com.sample.order.exception.ApplicationException;
import com.sample.order.exception.OrderAccessException;
import com.sample.order.httprequest.HttpRequest;
import com.sample.order.model.Order;
import com.sample.order.model.OrderItem;
import com.sample.order.repository.OrderItemRepository;
import com.sample.order.repository.OrderRepository;
import com.sample.order.requesthelper.PrepareOrderRequest;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final HttpRequest request;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PrepareOrderRequest prepareOrderRequest;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(HttpRequest request, ModelMapper modelMapper, OrderRepository orderRepository, OrderItemRepository orderItemRepository, PrepareOrderRequest prepareOrderRequest) {
        this.request = request;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.prepareOrderRequest = prepareOrderRequest;
    }

    public OrderDTO placeOrder(Long userId, Long addressId,String paymentMethod) throws ApplicationException {
        logger.info("Placing order for userId: {}, addressId: {}, paymentMethod: {}", userId, addressId, paymentMethod);

        logger.info("request to get user is being made for userId: {}", userId);
        UserDTO user = request.getUserById(userId);

        logger.info("request to get address is being made for userId: {}, addressId: {}", userId, addressId);
        AddressDTO address = request.getAddressByUserIdAndAddressId(userId,addressId);

        logger.info("request to get cart is being made for userId: {}", userId);
        CartDTO cart = request.getCartByUserId(userId);

        logger.info("requesting to prepare order with user: {}, address: {}, cart: {}, paymentMethod: {}", user, address, cart, paymentMethod);
        OrderDTO orderRespose = prepareOrderRequest.prepareRequest(user, address, cart, paymentMethod);

        //Emptying cart
        logger.info("request to empty cart for userId: {}, cartId: {}", userId, cart.getCartId());
        request.emptyCart(userId,cart.getCartId());

        return orderRespose;
    }

    public OrderDTO getByUserIdAndOrderId(Long userId, Long orderId) throws ApplicationException {

        logger.info("requesting order for userId: {}, orderId: {}", userId, orderId);
        UserDTO user = request.getUserById(userId);

        Optional<Order> order;

        try {
            logger.info("Fetching order with userId: {}, orderId: {}", userId, orderId);
            order = orderRepository.findByUserIdAndOrderId(userId, orderId);
        } catch (DataAccessResourceFailureException e) {
            logger.error("Error accessing order data for userId: {}, orderId: {}", userId, orderId, e);
            throw new OrderAccessException("Database access error while fetching order for userId: " + userId + " and orderId: " + orderId, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error("Unexpected error while fetching order for userId: {}, orderId: {}", userId, orderId, e);
            throw new ApplicationException("Unexpected error while fetching order for userId: " + userId + " and orderId: " + orderId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Order fetched successfully for userId: {}, orderId: {}", userId, orderId);

        Order orderFromDB;
        if (order.isPresent()) {
            orderFromDB = order.get();
            logger.info("Order found for userId: {}, orderId: {}", userId, orderId);
        } else {
            logger.warn("No order found for userId: {}, orderId: {}", userId, orderId);
            return null;
        }

        return modelMapper.map(orderFromDB, OrderDTO.class);

    }

    public List<OrderDTO> getByUserId(Long userId) throws ApplicationException {
        logger.info("Fetching orders for userId: {}", userId);
        UserDTO user = request.getUserById(userId);

        List<Order> orders;
        try {
            orders = orderRepository.findAllByUserId(userId);
            logger.info("Orders fetched successfully for userId: {}, total orders: {}", userId, orders.size());
        }catch (DataAccessResourceFailureException e){
            logger.error("Error accessing order data for userId: {}", userId, e);
            throw new OrderAccessException("Database access error while fetching orders for userId: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            logger.error("Unexpected error while fetching orders for userId: {}", userId, e);
            throw new ApplicationException("Unexpected error while fetching orders for userId: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<OrderDTO> orderDTOS = orders.stream().map(order->modelMapper.map(order,OrderDTO.class)).toList();

        return orderDTOS;
    }
}
