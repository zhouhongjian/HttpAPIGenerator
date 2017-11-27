package com.zhj;

/**
 * Created by zhj on 2017/11/28.
 */
public class InvokeResult<T> {
    private boolean success;
    private int code;
    private String description;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public InvokeResult<T> success(T data) {
        this.success = true;
        this.data = data;
        return this;
    }

    public InvokeResult<T> fail(int code,String description) {
        this.success = false;
        this.code = code;
        this.description = description;
        return this;
    }

    public InvokeResult<T> code(int code) {
        this.code = code;
        return this;
    }

    public InvokeResult<T> description(String description) {
        this.description = description;
        return this;
    }

    public InvokeResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public static <T> InvokeResult<T> create() {

        return new InvokeResult<T>();
    }

}
