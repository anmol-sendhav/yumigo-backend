package com.yumigo.entity;

import com.yumigo.enums.Role;
import com.yumigo.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Target;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="users")
@Entity
public class User extends BaseEntity{

    private String name;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    private String phone;
    @Column(nullable = false)
    private String passwordHash;
    private boolean emailVerified;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
}
