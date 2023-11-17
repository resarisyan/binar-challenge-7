package com.binaracademy.authservice.dto.request;

import com.binaracademy.authservice.validation.FieldExistence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerRequest {
    @NotBlank
    @FieldExistence(tableName = "users", fieldName = "username", shouldExist = false, message = "Username already exists")
    private String username;

    @NotBlank
    @Email
    @FieldExistence(tableName = "users", fieldName = "email", shouldExist = false, message = "Email already exists")
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;
}
