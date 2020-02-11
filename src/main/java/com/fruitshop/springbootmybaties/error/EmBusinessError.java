package com.fruitshop.springbootmybaties.error;

public enum EmBusinessError implements CommonError {
    //1000开头为通用错误类型
    UNKNOWN_ERROR(10000,"未知错误"),
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),


    //2000开头为用户信息相关错误类型
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户账号或密码错误"),
    USER_NOT_LOGIN(20003,"用户未登录"),
    USER_STATUS_ERROR(20004,"用户登录信息异常"),

    //3000开头为商品信息相关错误类型
    ITEM_NOT_EXIST(30001,"商品不存在"),
    ITEM_NOT_ENOUGH(30002,"商品库存不足"),
    ITEM_NOT_SALE(30003,"商品已下架"),

    //4000开头为商铺信息相关错误类型
    STORE_NOT_EXIST(40001,"商铺不存在"),
    STORE_LOGIN_FAIL(40002,"商铺账号或密码错误"),
    STORE_NOT_LOGIN(40003,"商铺未登录"),

    //5000开头为订单信息相关错误类型
    ORDER_NOT_EXIST(50001,"订单不存在"),
    ORDER_STATUS_ERROR(50002,"订单状态出现异常"),

    //6000开头为购物车信息相关错误类型
    CART_NOT_EXIST(60001,"购物车记录不存在"),
    CART_STATUS_ERROR(60002,"购物车状态异常"),
    ;

    private EmBusinessError(int errCode,String errMsg){
        this.errCode=errCode;
        this.errMsg=errMsg;
    }

    private int errCode;
    private String errMsg;

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
