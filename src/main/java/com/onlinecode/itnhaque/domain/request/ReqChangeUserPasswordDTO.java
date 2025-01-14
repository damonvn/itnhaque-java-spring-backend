package com.onlinecode.itnhaque.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChangeUserPasswordDTO {
    private int id;
    private String oldPassword;
    private String newPassword;
}
