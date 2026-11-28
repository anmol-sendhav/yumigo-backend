package com.yumigo.service.impl;

import com.yumigo.dto.request.address.AddressRequest;
import com.yumigo.dto.response.address.AddressResponse;
import com.yumigo.entity.Address;
import com.yumigo.entity.User;
import com.yumigo.exception.ResourceNotFoundException;
import com.yumigo.repository.AddressRepository;
import com.yumigo.repository.UserRepository;
import com.yumigo.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public AddressResponse create(Long userId, AddressRequest r) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Address a = new Address();
        a.setUser(user);
        a.setLabel(r.label());
        a.setLine1(r.line1());
        a.setLine2(r.line2());
        a.setCity(r.city());
        a.setState(r.state());
        a.setPinCode(r.pinCode());
        a.setLatitude(r.latitude());
        a.setLongitude(r.longitude());
        a.setDefaultAddress(r.defaultAddress());
        return toResponse(addressRepository.save(a));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AddressResponse> list(Long userId) {
        return addressRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse).toList();
    }

    private AddressResponse toResponse(Address a) {
        return new AddressResponse(a.getId(), a.getLabel(), a.getLine1(), a.getLine2(),
                a.getCity(), a.getState(), a.getPinCode(), a.getLatitude(),
                a.getLongitude(), a.isDefaultAddress());
    }
}
