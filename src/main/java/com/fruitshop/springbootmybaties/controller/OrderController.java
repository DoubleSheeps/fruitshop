package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.controller.viewobject.OrderVO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import com.fruitshop.springbootmybaties.service.OrderService;
import com.fruitshop.springbootmybaties.service.model.LogisticsModel;
import com.fruitshop.springbootmybaties.service.model.OrderModel;
import com.fruitshop.springbootmybaties.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class OrderController extends BaseController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "amount")Integer amount){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");


        OrderModel orderModel=orderService.createOrder(itemId,userModel.getId(),amount);
        return CommonReturnType.create(orderModel);
    }

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getOrders(){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        List<OrderModel> orderModelList = orderService.getOrders(userModel.getId());
        List<OrderVO> orderVOList = orderModelList.stream().map(orderModel -> {
            OrderVO orderVO = this.convertFromModel(orderModel);
            return orderVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(orderVOList);
    }


    @RequestMapping(value = "/complete",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType completeOrder(@RequestParam(name = "id") String id,
                                          @RequestParam(name = "paymentMethod") Integer paymentMethod,
                                          @RequestParam(name = "address") String address){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        LogisticsModel logisticsModel = orderService.completeOrder(id,userModel.getId(),paymentMethod,address);
        return CommonReturnType.create(logisticsModel);

    }

    @RequestMapping(value = "/cancel",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType cancelOrder(@RequestParam(name = "id") String id){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        orderService.cancelOrder(id,userModel.getId());
        return CommonReturnType.create(null);
    }

    private OrderVO convertFromModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderModel,orderVO);
        return orderVO;
    }

}
