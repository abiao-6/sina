//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.abiao.web.infrastructure.service;

import com.abiao.web.infrastructure.model.ErrorCode;
import com.abiao.web.infrastructure.model.ErrorCodeDescriptor;

public class ServiceValidationException extends ServiceException implements ErrorCodeDescriptor {
    private ErrorCode errorDescriptor;

    public ServiceValidationException() {
    }

    public ServiceValidationException(String message) {
        super(message);
    }

    public ServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceValidationException(Throwable cause) {
        super(cause);
    }

    public ServiceValidationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorDescriptor = errorCode;
    }

    public ErrorCode getErrorDescriptor() {
        return this.errorDescriptor;
    }
}
