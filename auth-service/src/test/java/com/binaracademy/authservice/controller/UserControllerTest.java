package com.binaracademy.authservice.controller;

import com.binaracademy.authservice.dto.request.UpdateUserRequest;
import com.binaracademy.authservice.dto.response.UserResponse;
import com.binaracademy.authservice.dto.response.base.APIResponse;
import com.binaracademy.authservice.dto.response.base.APIResultResponse;
import com.binaracademy.authservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    @Test
    void testUpdateUser() {
        UpdateUserRequest request = new UpdateUserRequest("newUsername", "newEmail", "newPassword");

        doNothing().when(userService).updateUser(request);

        ResponseEntity<APIResultResponse<UserResponse>> result = userController.updateUser(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User successfully updated", Objects.requireNonNull(result.getBody()).getMessage());
        assertNull(result.getBody().getResults());

        verify(userService).updateUser(request);
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).deleteUser();

        ResponseEntity<APIResponse> result = userController.deleteUser();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("User successfully deleted",  Objects.requireNonNull(result.getBody()).getMessage());

        verify(userService).deleteUser();
    }

}
