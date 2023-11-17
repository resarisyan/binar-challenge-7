package com.binaracademy.commerceservice.service;

import com.binaracademy.commerceservice.dto.request.CreateProductRequest;
import com.binaracademy.commerceservice.dto.request.UpdateProductReqeust;
import com.binaracademy.commerceservice.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductResponse addNewProduct(CreateProductRequest request);
    void updateProduct(String productName, UpdateProductReqeust request);
    void deleteProduct(String productName);
    ProductResponse getProductDetail(String selectedProductName);
    Page<ProductResponse> getProductsWithPagination(Pageable pageable);
}
