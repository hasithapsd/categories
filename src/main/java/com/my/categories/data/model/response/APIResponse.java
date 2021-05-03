package com.my.categories.data.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

    private T data;
    private List<ErrorResponse> error = new ArrayList<>();
    @JsonProperty("has_error")
    private boolean hasError;

    public boolean isHasError() {
        return !CollectionUtils.isEmpty(error);
    }
}