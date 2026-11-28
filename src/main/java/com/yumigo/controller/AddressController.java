package com.yumigo.controller;

import com.yumigo.dto.request.address.AddressRequest;
import com.yumigo.dto.response.address.AddressResponse;
import com.yumigo.security.CustomUserDetails;
import com.yumigo.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> create(@Valid @RequestBody AddressRequest request,
                                                  @AuthenticationPrincipal CustomUserDetails p) {
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.create(p.getId(), request));
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> list(@AuthenticationPrincipal CustomUserDetails p) {
        return ResponseEntity.ok(addressService.list(p.getId()));
    }
}
