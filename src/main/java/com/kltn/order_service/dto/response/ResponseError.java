package com.kltn.order_service.dto.response;

@SuppressWarnings("rawtypes")
public class ResponseError extends ResponseData {

    public ResponseError(int status, String message) {
        super(status, message);
    }
}
