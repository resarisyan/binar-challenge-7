package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.request.UpdateUserRequest;
import com.binaracademy.authservice.dto.response.UserResponse;
import com.binaracademy.authservice.dto.response.base.APIResponse;
import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import com.binaracademy.authservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/user", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    @PutMapping("/")
    @Schema(name = "UpdateUserRequest", description = "Update user request body")
    @Operation(summary = "Endpoint to handle update user")
    public ResponseEntity<APIResultResponse<UserResponse>> updateUser(@RequestBody @Valid UpdateUserRequest request){
        userService.updateUser(request);
        APIResultResponse<UserResponse> responseDTO = new APIResultResponse<>(
                HttpStatus.OK,
                "User successfully updated"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/")
    @Schema(name = "DeleteUserRequest", description = "Delete user request body")
    @Operation(summary = "Endpoint to handle delete user")
    public ResponseEntity<APIResponse> deleteUser(){
        userService.deleteUser();
        APIResponse responseDTO =  new APIResponse(
                HttpStatus.OK,
                "User successfully deleted"
        );
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
