package com.roadmap.fase1.dto;

import com.roadmap.fase1.model.UserRole;

public record RegisterDTO(
        String name,
        String email,
        String password,
        String phone,
        UserRole role
) {
}
