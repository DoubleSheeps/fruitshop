package com.fruitshop.springbootmybaties.service;

import com.fruitshop.springbootmybaties.service.model.StoreModel;
import com.fruitshop.springbootmybaties.service.model.UserModel;

public interface StoreService {

    //通过用户telphone获取用户对象的方法
    StoreModel getStoreByTelphone(String telphone);

    //商户注册手机校验
    Boolean isRegisteredByTelphone(String telphone);

    //商户注册名称校验
    Boolean isRegisterByStoreName(String storeName);

    //注册
    void register(StoreModel storeModel);

    /**
     * 登录
     * @param telphone 商家注册手机
     * @param encrptPassword 商家加密后的密码
     */
    StoreModel validateLogin(String telphone, String encrptPassword);



}
