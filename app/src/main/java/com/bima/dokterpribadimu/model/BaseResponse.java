package com.bima.dokterpribadimu.model;

import com.bima.dokterpribadimu.utils.Constants.Status;

/**
 * Created by apradanas on 3/5/16.
 */
public class BaseResponse<T> {

    private String status;
    private T data;
    private String message;

    public Status getStatus() {
        return Status.valueOf(status.toUpperCase());
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
