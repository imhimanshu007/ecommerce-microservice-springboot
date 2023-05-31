package com.gurukul.orderservice.service.impl;

import com.gurukul.orderservice.dto.InventoryResponse;
import com.gurukul.orderservice.dto.OrderLineItemsDto;
import com.gurukul.orderservice.dto.OrderRequest;
import com.gurukul.orderservice.model.Order;
import com.gurukul.orderservice.model.OrderLineItems;
import com.gurukul.orderservice.repository.OrderRepository;
import com.gurukul.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToEntity)
                .toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();


        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        assert inventoryResponseArray != null;
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        if(allProductsInStock){
            orderRepository.save(order);
        }
        else{
            throw new IllegalArgumentException("Product is not in Stock, please try again");
        }

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
