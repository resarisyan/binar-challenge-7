package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.response.base.APIResponse;
import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductService productService;

    @Test
    void testAddNewProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setProductName("testName");
        request.setPrice(10000.0);
        ProductResponse response = new ProductResponse();
        response.setProductName(request.getProductName());
        response.setPrice(request.getPrice());

        when(productService.addNewProduct(request)).thenReturn(response);

        ResponseEntity<APIResultResponse<ProductResponse>> result = productController.createNewProduct(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("Product successfully created", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(productService).addNewProduct(request);
    }

    @Test
    void testGetProductsWithPagination_Success() {
        Page<ProductResponse> response = Page.empty();

        when(productService.getProductsWithPagination(any(Pageable.class))).thenReturn(response);

        ResponseEntity<APIResultResponse<Page<ProductResponse>>> result = productController.getProductsWithPagination(0);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Products successfully retrieved", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(productService).getProductsWithPagination(PageRequest.of(0, 5));
    }

    @Test
    void testDeleteProduct() {
        String productName = "testProduct";

        doNothing().when(productService).deleteProduct(productName);

        ResponseEntity<APIResponse> result = productController.deleteProduct(productName);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Product successfully deleted", Objects.requireNonNull(result.getBody()).getMessage());
        verify(productService).deleteProduct(productName);
    }

    @Test
    void testGetProductDetail() {
        String productName = "testProduct";
        ProductResponse response = new ProductResponse("testProduct", 1000.0, new MerchantResponse("testMerchant", "Location", true));

        when(productService.getProductDetail(productName)).thenReturn(response);

        ResponseEntity<APIResultResponse<ProductResponse>> result = productController.getProductDetail(productName);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Product successfully retrieved", Objects.requireNonNull(result.getBody()).getMessage());
        assertEquals(response, result.getBody().getResults());

        verify(productService).getProductDetail(productName);
    }

    @Test
    void testUpdateProduct() {
        String productName = "testProduct";
        UpdateProductReqeust request = new UpdateProductReqeust();
        request.setProductName("testName");
        request.setPrice(10000.0);

        doNothing().when(productService).updateProduct(productName, request);

        ResponseEntity<APIResponse> result = productController.updateProduct(productName, request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Product successfully updated", Objects.requireNonNull(result.getBody()).getMessage());

        verify(productService).updateProduct(productName, request);
    }
}
