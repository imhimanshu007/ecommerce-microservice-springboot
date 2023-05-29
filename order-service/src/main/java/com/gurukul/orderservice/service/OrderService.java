package com.gurukul.orderservice.service;

import com.gurukul.orderservice.dto.OrderRequest;

public interface OrderService {
    public void placeOrder(OrderRequest orderRequest);
}
