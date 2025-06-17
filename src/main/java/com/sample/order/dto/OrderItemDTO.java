package com.sample.order.dto;

import java.math.BigDecimal;

public class OrderItemDTO {

    private Long orderItemId;

    private BigDecimal price;

    private Long productId;

    private int quantity;

//    private OrderDTO order;


    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

//    public OrderDTO getOrder() {
//        return order;
//    }
//
//    public void setOrder(OrderDTO order) {
//        this.order = order;
//    }
}
