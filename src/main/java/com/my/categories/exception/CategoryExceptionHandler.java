package com.my.categories.exception;

import com.my.categories.data.model.response.APIResponse;
import com.my.categories.data.model.response.ErrorResponse;
import com.my.categories.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

public class CategoryExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleException(Exception e) {

        ErrorCodes errorCode = ErrorCodes.SERVICE_FAILURE;
        String errorMessage = "An internal error has occurred while performing the request";

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(errorMessage);
        errorResponse.setAdditionalInfo(e.getMessage());
        errorResponse.setCode(errorCode.getCode());

        APIResponse apiResponse = ResponseUtils.createFailureResponse(Collections.singletonList(errorResponse));

        LOG.error("Internal server error occurred", e);
        return new ResponseEntity<>(apiResponse, errorCode.getStatus());
    }
}
