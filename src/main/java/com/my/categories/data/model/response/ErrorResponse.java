package com.my.categories.data.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private String code;
    private String additionalInfo;
}