package com.yumigo.service;

import com.yumigo.dto.response.user.UserResponse;

public interface UserService {

    UserResponse getCurrentUser(Long id);
}
