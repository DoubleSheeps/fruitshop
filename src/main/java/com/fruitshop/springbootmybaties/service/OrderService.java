package com.fruitshop.springbootmybaties.service;

import com.fruitshop.springbootmybaties.service.model.LogisticsModel;
import com.fruitshop.springbootmybaties.service.model.OrderModel;

import java.util.List;

public interface OrderService {

    //创建订单
    OrderModel createOrder(Integer itemId, Integer userId, Integer amount);

    //查询订单
    List<OrderModel> getOrders(Integer userId);

    //支付
    LogisticsModel completeOrder(String id, Integer userId,Integer paymentMethod, String address);

    //取消订单
    void cancelOrder(String id, Integer userId);

}
