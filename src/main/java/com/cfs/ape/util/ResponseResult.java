package com.cfs.ape.util;

public class ResponseResult<T> {



    private boolean success = true;
    private String errMsg;
    private String errCode;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult(){

    }

    public ResponseResult(boolean success, String errMsg, String errCode) {
        this.success = success;
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    public ResponseResult(String errMsg, String errCode) {
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    public ResponseResult(boolean success, String errCode) {
        this.success = success;
        this.errCode = errCode;
    }

    public static ResponseResult<Object> success() {
        return new ResponseResult();
    }

    public static ResponseResult<Object> errMsg(String errMsg) {
        ResponseResult result = new ResponseResult(false, errMsg, "E00001");
        return result;
    }

    public static ResponseResult<Object> errMsg(String errMsg, Object data) {
        ResponseResult result = new ResponseResult(false, errMsg, "E00001");
        result.setData(data);
        return result;
    }

    public static ResponseResult<Object> error(String errCode, String errMsg) {
        ResponseResult result = new ResponseResult(false, errCode, errMsg);
        return result;
    }

    public static ResponseResult<Object> errCode(String errCode) {
        ResponseResult result = new ResponseResult(false, errCode);
        result.setSuccess(false);
        return result;
    }

    public static ResponseResult<Object> success(Object data) {
        ResponseResult<Object> result = new ResponseResult<>();
        result.setData(data);
        return result;
    }

    public static ResponseResult<Object> err() {
        ResponseResult<Object> result = new ResponseResult<>();
        result.setSuccess(false);
        return result;
    }
}
