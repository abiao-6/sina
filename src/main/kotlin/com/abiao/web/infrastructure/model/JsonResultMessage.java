//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.abiao.web.infrastructure.model;

import java.io.Serializable;

public class JsonResultMessage<T> implements Serializable {
    public static final int FLAG_SUCCESS = 0;
    public static final int FLAG_FAILED = -1;
    private int flag = 0;
    private String message;
    private String cause;
    private T data;

    public static JsonResultMessage of(Throwable throwable) {
        JsonResultMessage result = new JsonResultMessage(-1, throwable.getMessage(), (Object) null);
        Throwable cause = throwable.getCause();
        if (cause != null) {
            result.setCause(cause.getMessage());
        }

        return result;
    }

    public static JsonResultMessage of(ErrorCode errorCode) {
        return new JsonResultMessage(errorCode.getCode(), errorCode.getMessage(), (Object) null);
    }

    public static JsonResultMessage createFailure() {
        return createFailure("");
    }

    public static JsonResultMessage createFailure(String message) {
        return new JsonResultMessage(-1, message, (Object) null);
    }

    public static JsonResultMessage createSuccess() {
        return createSuccess((Object) null);
    }

    public static <T> JsonResultMessage<T> createSuccess(T data) {
        return createSuccess("", data);
    }

    public static <T> JsonResultMessage<T> createSuccess(String message, T data) {
        return new JsonResultMessage(0, message, data);
    }

    public JsonResultMessage() {
    }

    public JsonResultMessage(int code, String message, T data) {
        this.flag = code;
        this.message = message;
        this.data = data;
    }

    public boolean hasError() {
        return this.getFlag() != 0;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JsonResultMessage)) {
            return false;
        } else {
            JsonResultMessage<?> other = (JsonResultMessage) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getFlag() != other.getFlag()) {
                return false;
            } else {
                label49:
                {
                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    if (this$message == null) {
                        if (other$message == null) {
                            break label49;
                        }
                    } else if (this$message.equals(other$message)) {
                        break label49;
                    }

                    return false;
                }

                Object this$cause = this.getCause();
                Object other$cause = other.getCause();
                if (this$cause == null) {
                    if (other$cause != null) {
                        return false;
                    }
                } else if (!this$cause.equals(other$cause)) {
                    return false;
                }

                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof JsonResultMessage;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + this.getFlag();
        Object $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        Object $cause = this.getCause();
        result = result * 59 + ($cause == null ? 43 : $cause.hashCode());
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        return result;
    }

    public int getFlag() {
        return this.flag;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCause() {
        return this.cause;
    }

    public T getData() {
        return this.data;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
        return "JsonResultMessage(flag=" + this.getFlag() + ", message=" + this.getMessage() + ", cause=" + this.getCause() + ", data=" + this.getData() + ")";
    }
}
