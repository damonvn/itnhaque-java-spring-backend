package com.onlinecode.itnhaque.domain.response.error;

import com.onlinecode.itnhaque.domain.response.RestResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResError {
    private RestResponse<NullPointerException> data;
}
