package com.homework.zhouhao.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {

    ADMIN("admin"),
    USER("user");

    private String role;

    public static boolean isAdmin(String role) {
        return ADMIN.getRole().equals(role);
    }
}
