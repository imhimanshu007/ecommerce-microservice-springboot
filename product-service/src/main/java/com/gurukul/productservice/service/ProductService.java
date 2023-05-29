package com.gurukul.productservice.service;

import com.gurukul.productservice.dto.ProductRequest;
import com.gurukul.productservice.dto.ProductResponse;

import java.util.List;

public interface ProductService {
    public void createProduct(ProductRequest productRequest);
    List<ProductResponse> getAllProducts();
}
