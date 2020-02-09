package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.controller.viewobject.UserVO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import com.fruitshop.springbootmybaties.service.CartService;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.UserService;
import com.fruitshop.springbootmybaties.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
@RequestMapping("/user")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    //@Autowired
    //private CartService cartService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户登录接口
    @RequestMapping(value = "/loginbytelphone",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType loginByTelphone(@RequestParam(name = "telphone")String telphone,
                                            @RequestParam(name = "password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(telphone)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLoginByTelphone(telphone,this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        //单位：秒
        //this.httpServletRequest.getSession().setMaxInactiveInterval(60);

        return CommonReturnType.create(null);
    }
    @RequestMapping(value = "/loginbyemail",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType loginByEmail(@RequestParam(name = "email")String email,
                                            @RequestParam(name = "password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，用来校验用户登录是否合法
        UserModel userModel = userService.validateLoginByEmail(email,this.EncodeByMd5(password));

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        //单位：秒
        //this.httpServletRequest.getSession().setMaxInactiveInterval(60);

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType register(@RequestParam(name = "telphone")String telphone,
                                     @RequestParam(name = "otpCode")String otpCode,
                                     @RequestParam(name = "name")String name,
                                     @RequestParam(name = "gender")Integer gender,
                                     @RequestParam(name = "age")Integer age,
                                     @RequestParam(name = "email")String email,
                                     @RequestParam(name = "password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和otpCode相符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
        }else if(userService.isRegisteredByEmail(email)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"电子邮箱已注册");
        }else {
                UserModel userModel = new UserModel();
                userModel.setName(name);
                userModel.setAge(age);
                userModel.setGender(gender);
                userModel.setEmail(email);
                userModel.setTelphone(telphone);
                userModel.setEncrptPassword(this.EncodeByMd5(password));
                userModel.setCreateTime(new Date());
                userModel.setUpdateTime(new Date());
                userService.register(userModel);
                return CommonReturnType.create(null);
        }
    }

    public String EncodeByMd5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //确定一个计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        //加密字符串
        return base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
    }


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType getOtp(@RequestParam(name = "telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Boolean isRegistered = userService.isRegisteredByTelphone(telphone);
        if(isRegistered){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("该手机号已注册"));
        }
        this.sendOtpCode(telphone);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getUser() throws BusinessException {
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        //若获取的对应用户信息不存在
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        //将核心领域模型用户对象转化成可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    @RequestMapping("/logout")
    public CommonReturnType logout(){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        this.httpServletRequest.getSession().invalidate();
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType updateUser(@RequestParam(name = "name")String name,
                                       @RequestParam(name = "gender")Integer gender,
                                       @RequestParam(name = "age")Integer age,
                                       @RequestParam(name = "email")String email){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(gender);
        userModel.setEmail(email);
        userModel.setUpdateTime(new Date());
        UserModel userModel1 = userService.updateUser(userModel);
        UserVO userVO = this.convertFromModel(userModel1);
        return CommonReturnType.create(userVO);
    }

    @RequestMapping("/changePassword")
    public CommonReturnType changePassword(@RequestParam(name = "password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        if(this.EncodeByMd5(password).equals(userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"与近期密码相同，请重新设置密码");
        }
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userModel.setUpdateTime(new Date());
        userService.changePassword(userModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/forgotPassword")
    public CommonReturnType forgotPassword(@RequestParam(name = "telphone")String telphone){
        Boolean isRegistered = userService.isRegisteredByTelphone(telphone);
        if(!isRegistered){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        this.sendOtpCode(telphone);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/resetPassword")
    public CommonReturnType resetPassword(@RequestParam(name = "telphone") String telphone,
                                          @RequestParam(name = "otpCode") String otpCode,
                                          @RequestParam(name = "password")String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(telphone);
        if(!inSessionOtpCode.equals(otpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
        }
        UserModel userModel = userService.getUserByTelphone(telphone);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        if(this.EncodeByMd5(password).equals(userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"与近期密码相同，请重新设置密码");
        }
        userModel.setEncrptPassword(this.EncodeByMd5(password));
        userModel.setUpdateTime(new Date());
        userService.changePassword(userModel);
        return CommonReturnType.create(null);
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


    private UserVO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }
}
