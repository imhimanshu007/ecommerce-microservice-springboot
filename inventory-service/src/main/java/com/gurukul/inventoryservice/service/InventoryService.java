package com.gurukul.inventoryservice.service;

import com.gurukul.inventoryservice.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    public List<InventoryResponse> isInStock(List<String> skuCode);
}
