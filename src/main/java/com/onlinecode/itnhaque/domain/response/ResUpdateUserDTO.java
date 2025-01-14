package com.onlinecode.itnhaque.domain.response;

import java.time.Instant;

import com.onlinecode.itnhaque.util.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
    private int id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private RoleUser role;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleUser {
        private long id;
        private String name;
    }
}