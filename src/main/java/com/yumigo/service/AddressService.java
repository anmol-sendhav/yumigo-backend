package com.yumigo.service;

import com.yumigo.dto.request.address.AddressRequest;
import com.yumigo.dto.response.address.AddressResponse;

import java.util.List;

public interface AddressService {

    public AddressResponse create(Long userId, AddressRequest r);
    public List<AddressResponse> list(Long userId);
}
