package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import com.fruitshop.springbootmybaties.service.StoreService;
import com.fruitshop.springbootmybaties.service.model.StoreModel;
import com.fruitshop.springbootmybaties.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/store")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class StoreController extends BaseController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType loginByTelphone(@RequestParam(name = "telphone")String telphone,
                                            @RequestParam(name = "password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，用来校验用户登录是否合法
        StoreModel storeModel = storeService.validateLogin(telphone,this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_STORE_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_STORE",storeModel);
        //单位：秒
        //this.httpServletRequest.getSession().setMaxInactiveInterval(60);

        return CommonReturnType.create(null);
    }

    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Boolean isRegistered = storeService.isRegisteredByTelphone(telphone);
        if(isRegistered){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("该手机号已注册"));
        }
        this.sendOtpCode(telphone);
        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "storeName")String storeName,
                                     @RequestParam(name = "address")String address,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和otpCode相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "短信验证码错误");
        }else if(storeService.isRegisterByStoreName(storeName)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商铺名称已被注册");
        }else{
            StoreModel storeModel = new StoreModel();
            storeModel.setTelphone(telphone);
            storeModel.setStoreName(storeName);
            storeModel.setAddress(address);
            storeModel.setEncrptPassword(this.EncodeByMd5(password));
            storeModel.setCreateTime(new Date());
            storeModel.setUpdateTime(new Date());
            storeService.register(storeModel);
            return CommonReturnType.create(null);
        }
    }

    private void sendOtpCode(String telphone){
        Random random = new Random();
        int randomInt = random.nextInt(8999);
        randomInt += 1000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码同对应用户的手机号关联，使用httpsession的方式
        httpServletRequest.getSession().setAttribute(telphone,otpCode);


        //将OTP验证码通过短信通道发送给用户
        System.out.println("telphone = " + telphone + "& otpCode = " + otpCode);
    }

    private String EncodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定一个计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
    }

}
