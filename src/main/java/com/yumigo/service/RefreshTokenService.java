package com.yumigo.service;

import com.yumigo.entity.RefreshToken;
import com.yumigo.entity.User;

public interface RefreshTokenService {

    RefreshToken create(User user);
    public RefreshToken rotate(String tokenValue);
    public void revoke(String tokenValue, Long userId);
}
