package com.gurukul.orderservice.service.impl;

import com.gurukul.orderservice.dto.OrderLineItemsDto;
import com.gurukul.orderservice.dto.OrderRequest;
import com.gurukul.orderservice.model.Order;
import com.gurukul.orderservice.model.OrderLineItems;
import com.gurukul.orderservice.repository.OrderRepository;
import com.gurukul.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToEntity)
                .toList();
        order.setOrderLineItems(orderLineItems);
        orderRepository.save(order);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto){
        return OrderLineItems.builder()
                .id(orderLineItemsDto.getId())
                .quantity(orderLineItemsDto.getQuantity())
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();
    }
}
