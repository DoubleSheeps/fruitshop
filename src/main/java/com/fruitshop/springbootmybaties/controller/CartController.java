package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.controller.viewobject.CartVO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import com.fruitshop.springbootmybaties.service.CartService;
import com.fruitshop.springbootmybaties.service.model.CartModel;
import com.fruitshop.springbootmybaties.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class CartController extends BaseController {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private CartService cartService;

    @RequestMapping(value = "add",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType addToCart(@RequestParam(name = "itemId") Integer itemId,
                                      @RequestParam(name = "amount") Integer amount){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");

        cartService.addToCart(itemId,userModel.getId(),amount);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "delete",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType deleteFromCart(@RequestParam(name = "id") Integer id){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        cartService.deleteFromCart(id,userModel.getId());
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "list",method = {RequestMethod.GET})
    public CommonReturnType list(){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        List<CartModel> cartModelList = cartService.list(userModel.getId());
        List<CartVO> cartVOList = cartModelList.stream().map(cartModel -> {
            CartVO cartVO = this.convertFromModel(cartModel);
            return cartVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(cartVOList);
    }
    @RequestMapping(value = "update",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType update(@RequestParam(name = "id") Integer id,
                                   @RequestParam(name = "amount") Integer amount){
        //获取用户的登录信息
        Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_USER_LOGIN");
        if(isLogin == null || !isLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel)httpServletRequest.getSession().getAttribute("LOGIN_USER");
        CartModel cartModel = cartService.updateCart(id,amount,userModel.getId());
        CartVO cartVO = this.convertFromModel(cartModel);
        return CommonReturnType.create(cartVO);
    }


    private CartVO convertFromModel(CartModel cartModel){
        if(cartModel == null){
            return null;
        }
        CartVO cartVO = new CartVO();
        BeanUtils.copyProperties(cartModel,cartVO);
        return cartVO;
    }

}
