package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";
    private final static Logger logger = LoggerFactory.getLogger(BaseController.class);

    //定义exceptionhandler解决未被controller层吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception exception){
        Map<String,Object> responseData = new HashMap<>();
        if(exception instanceof BusinessException){
            BusinessException businessException = (BusinessException)exception;
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
        }else {
            logger.error("【系统异常】{}",exception.getMessage());
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(CommonReturnType.STATUS_FAIL,responseData);
    }
}
