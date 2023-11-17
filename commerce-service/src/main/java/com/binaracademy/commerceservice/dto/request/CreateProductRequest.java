package com.binaracademy.commerceservice.dto.request;

import com.binaracademy.commerceservice.validation.FieldExistence;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank
    @FieldExistence(tableName = "product", fieldName = "product_name", shouldExist = false, message = "Product name already exists")
    private String productName;
    @NotNull
    @Max(1000000)
    private Double price;
}
