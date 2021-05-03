package com.my.categories.utils;


import com.my.categories.data.model.response.APIResponse;
import com.my.categories.data.model.response.ErrorResponse;

import java.util.List;

public final class ResponseUtils {

    private ResponseUtils() {
    }

    public static APIResponse createSucessResponse(Object data) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setData(data);
        return apiResponse;
    }

    public static APIResponse createFailureResponse(List<ErrorResponse> errors) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setError(errors);
        return apiResponse;
    }
}