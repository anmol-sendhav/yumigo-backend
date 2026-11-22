package com.yumigo.service.impl;

import com.yumigo.dto.response.user.UserResponse;
import com.yumigo.entity.User;
import com.yumigo.exception.ResourceNotFoundException;
import com.yumigo.mapper.UserMapper;
import com.yumigo.repository.UserRepository;
import com.yumigo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id",id));
        return userMapper.toUserResponse(user);
    }
}
