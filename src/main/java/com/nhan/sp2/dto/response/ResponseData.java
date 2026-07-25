package com.nhan.sp2.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResponseData<T> {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseData(final int status, final String message, final T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


}
