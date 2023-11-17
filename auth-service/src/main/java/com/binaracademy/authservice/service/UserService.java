package com.binaracademy.authservice.service;

import com.binaracademy.authservice.dto.request.UpdateUserRequest;

public interface UserService {
    void updateUser(UpdateUserRequest request);
    void deleteUser();
}
