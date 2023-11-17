package com.binaracademy.commerceservice.service;

import com.binaracademy.commerceservice.client.UserClient;
import com.binaracademy.commerceservice.dto.request.CreateProductRequest;
import com.binaracademy.commerceservice.dto.request.UpdateProductReqeust;
import com.binaracademy.commerceservice.dto.response.MerchantResponse;
import com.binaracademy.commerceservice.dto.response.ProductResponse;
import com.binaracademy.commerceservice.entity.Merchant;
import com.binaracademy.commerceservice.entity.Product;
import com.binaracademy.commerceservice.entity.User;
import com.binaracademy.commerceservice.exception.AccessDeniedException;
import com.binaracademy.commerceservice.exception.DataNotFoundException;
import com.binaracademy.commerceservice.exception.ServiceBusinessException;
import com.binaracademy.commerceservice.repository.MerchantRepository;
import com.binaracademy.commerceservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final UserClient userClient;
    private static final String PRODUCT_NOT_FOUND = "Product not found";
    private static final String MERCHANT_NOT_FOUND = "Merchant not found";
    private static final String ACCESS_DENIED = "You are not allowed to access this product";
    @Override
    public ProductResponse addNewProduct(CreateProductRequest request) {
        ProductResponse productResponse;
        try{
            log.info("Adding new product");
            User user = userClient.getDetail();
            Merchant merchant = merchantRepository.findFirstByUsername(user.getUsername())
                    .orElseThrow(() -> new DataNotFoundException(MERCHANT_NOT_FOUND));
            Product product = Product.builder()
                    .productName(request.getProductName())
                    .merchant(merchant)
                    .price(request.getPrice())
                    .build();
            productRepository.save(product);
            productResponse = ProductResponse.builder()
                    .productName(product.getProductName())
                    .merchant(MerchantResponse.builder()
                            .merchantName(merchant.getMerchantName())
                            .merchantLocation(merchant.getMerchantLocation())
                            .open(merchant.getOpen())
                            .build())
                    .price(product.getPrice())
                    .build();
        } catch (Exception e) {
            log.error("Failed to add new product");
            throw new ServiceBusinessException("Failed to add new product");
        }

        log.info("Product {} successfully added", productResponse.getProductName());
        return productResponse;
    }
    @Override
    public void updateProduct(String productName, UpdateProductReqeust request) {
        try {
            log.info("Updating product");
            User user = userClient.getDetail();
            Product existingProduct = productRepository.findByProductName(productName).orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND));
            if (!existingProduct.getMerchant().getUsername().equals(user.getUsername())) {
                throw new AccessDeniedException(ACCESS_DENIED);
            }

            existingProduct.setProductName(request.getProductName());
            existingProduct.setPrice(request.getPrice());
            productRepository.save(existingProduct);
            log.info("Product {} successfully updated", existingProduct.getProductName());
        } catch (AccessDeniedException | DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update product");
            throw new ServiceBusinessException("Failed to update product");
        }
    }
    @Override
    public void deleteProduct(String productName) {
        try {
            log.info("Deleting product");
            User user = userClient.getDetail();
            Product existingProduct = productRepository.findByProductName(productName).orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND));
            if (!existingProduct.getMerchant().getUsername().equals(user.getUsername())) {
                throw new AccessDeniedException(ACCESS_DENIED);
            }
            productRepository.delete(existingProduct);
            log.info("Product {} successfully deleted", existingProduct.getProductName());
        } catch (AccessDeniedException | DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to delete product");
            throw new ServiceBusinessException("Failed to delete product");
        }
    }
    @Override
    public ProductResponse getProductDetail(String productName) {
        try {
            log.info("Getting product detail");
            Product product = productRepository.findByProductName(productName).orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND));
            return ProductResponse.builder()
                    .productName(product.getProductName())
                    .merchant(MerchantResponse.builder()
                            .merchantName(product.getMerchant().getMerchantName())
                            .merchantLocation(product.getMerchant().getMerchantLocation())
                            .open(product.getMerchant().getOpen())
                            .build())
                    .price(product.getPrice())
                    .build();
        } catch (AccessDeniedException | DataNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            log.error("Failed to get product detail");
            throw new ServiceBusinessException("Failed to get product detail");
        }
    }
    @Override
    public Page<ProductResponse> getProductsWithPagination(Pageable pageable) {
        try {
            Page<Product> productPage = Optional.of(productRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(PRODUCT_NOT_FOUND));
            return  productPage.map(product -> ProductResponse.builder()
                    .productName(product.getProductName())
                    .merchant(MerchantResponse.builder()
                            .merchantName(product.getMerchant().getMerchantName())
                            .merchantLocation(product.getMerchant().getMerchantLocation())
                            .open(product.getMerchant().getOpen())
                            .build())
                    .price(product.getPrice())
                    .build());
        }  catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get all product");
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
