package com.cfs.ape.config;

import com.cfs.ape.util.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


@ControllerAdvice(basePackages = "com.cfs.ape.controller")
public class GlobalDefaultExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(DataIntegrityViolationException e) {
        return ResponseResult.errMsg(e.getMessage());
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    @ResponseBody
    public ResponseResult requestMaxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        return ResponseResult.errMsg("请上传不大于10M的照片");
    }



    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult argumentException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        String errorString = StringUtils.EMPTY;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorString = StringUtils.join(fieldError.getDefaultMessage());
        }
        return ResponseResult.errMsg(errorString);
    }

    @ResponseBody
    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseResult requestException(ServletRequestBindingException e) {
        logger.error(e.getMessage(), e);
        return ResponseResult.errMsg("请求头参数错误: "+e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(Exception e) {
        logger.error(e.getMessage(), e);
        return ResponseResult.errMsg("服务器错误，请找系统管理员上报此问题");
    }

}
