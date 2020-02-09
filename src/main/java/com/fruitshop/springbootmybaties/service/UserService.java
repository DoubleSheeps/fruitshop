package com.fruitshop.springbootmybaties.service;

import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.service.model.UserModel;

public interface UserService {
    //通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);

    //通过用户telphone获取用户对象的方法
    UserModel getUserByTelphone(String telphone);

    //用户注册手机校验
    Boolean isRegisteredByTelphone(String telphone);

    //用户注册手机校验
    Boolean isRegisteredByEmail(String email);

    //注册
    void register(UserModel userModel) throws BusinessException;

    //修改用户信息
    UserModel updateUser(UserModel userModel);

    void changePassword(UserModel userModel);


    /**
     * 登录
     * @param telphone 用户注册手机
     * @param encrptPassword 用户加密后的密码
     */
    UserModel validateLoginByTelphone(String telphone,String encrptPassword);

    /**
     * 登录
     * @param email 用户注册邮箱
     * @param encrptPassword 用户加密后的密码
     */
    UserModel validateLoginByEmail(String email,String encrptPassword);

}
