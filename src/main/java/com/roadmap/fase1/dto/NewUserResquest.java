package com.roadmap.fase1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserResquest {
    private String name;
    private String password;
    private String roleName;
    private String email;
    private String phone;
}
