package com.my.categories.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes {

    SERVICE_FAILURE("100", HttpStatus.INTERNAL_SERVER_ERROR,""),
    INVALID_REQUEST("101", HttpStatus.BAD_REQUEST, ""),
    RESOURCE_NOT_FOUND("102", HttpStatus.NOT_FOUND, ""),
    DUPLICATE_LINE_NUMBER("141", HttpStatus.BAD_REQUEST, "Duplicate line numbers are not allowed in the request"),
    UNABLE_TO_CREATE_ORDER("5001", HttpStatus.INTERNAL_SERVER_ERROR, "failed to save order"),
    INVALID_ORDER("5002", HttpStatus.BAD_REQUEST, "order null or invalid"),
    ORDER_NOT_FOUND_FOR_PARAMS("5081", HttpStatus.NOT_FOUND, "order not found for params, id:{0}, shop:{1}, customer:{2}"),
    FAILED_TO_FETCH_ORDER_FOR_PARAMS("5082",  HttpStatus.BAD_REQUEST,"failed to get order for params, id:{0}, shop:{1}, customer:{2}");



    private String code;
    private HttpStatus status;
    private String errorMessage;


    ErrorCodes(String code, HttpStatus status, String errorMessage) {
        this.code = code;
        this.status = status;
        this.errorMessage = errorMessage;
    }
}