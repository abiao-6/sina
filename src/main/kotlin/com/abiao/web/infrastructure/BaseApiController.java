//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.abiao.web.infrastructure;

import com.abiao.web.infrastructure.model.ErrorCode;
import com.abiao.web.infrastructure.model.ErrorCodeDescriptor;
import com.abiao.web.infrastructure.model.JsonResultMessage;
import com.abiao.web.infrastructure.service.ServiceValidationException;

public abstract class BaseApiController {
    public BaseApiController() {
    }

    protected JsonResultMessage success() {
        return this.success((String)null);
    }

    protected JsonResultMessage success(String message) {
        return this.success(message, (Object)null);
    }

    protected <T> JsonResultMessage<T> success(T data) {
        return this.success("", data);
    }

    protected <T> JsonResultMessage<T> success(String message, T data) {
        return this.result(0, message, data);
    }

    protected JsonResultMessage fail() {
        return this.fail("", (String)null);
    }

    protected JsonResultMessage fail(String message) {
        return this.fail(message, (String)null);
    }

    protected <T> JsonResultMessage<T> fail(String message, T data) {
        return this.result(-1, message, data);
    }

    protected JsonResultMessage fail(String message, String cause) {
        JsonResultMessage result = JsonResultMessage.createFailure(message);
        result.setCause(cause);
        return result;
    }

    protected JsonResultMessage error(ErrorCode errorCode) {
        return this.apiResult(errorCode.getCode(), errorCode.getMessage(), (Object)null);
    }

    protected JsonResultMessage error(ErrorCodeDescriptor descriptor) {
        ErrorCode errorDescriptor = descriptor.getErrorDescriptor();
        return this.error(errorDescriptor);
    }

    protected JsonResultMessage error(ServiceValidationException exception) {
        ErrorCode errorDescriptor = exception.getErrorDescriptor();
        return errorDescriptor == null ? this.error(errorDescriptor) : this.apiErrorResult(exception);
    }

    protected JsonResultMessage error(int flag, String message) {
        return this.result(flag, message, (Object)null);
    }

    protected <T> JsonResultMessage<T> result(int flag, String message, T data) {
        return new JsonResultMessage(flag, message, data);
    }

    protected JsonResultMessage apiErrorResult(Exception error) {
        return this.result(-1, error.getMessage(), (Object)null);
    }

    /** @deprecated */
    @Deprecated
    protected <T> JsonResultMessage<T> apiResult(int flag, String message, T data) {
        return this.result(flag, message, data);
    }
}
