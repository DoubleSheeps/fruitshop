package com.fruitshop.springbootmybaties.service.impl;

import com.fruitshop.springbootmybaties.dao.CartDOMapper;
import com.fruitshop.springbootmybaties.dataobject.CartDO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.service.CartService;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.model.CartModel;
import com.fruitshop.springbootmybaties.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartDOMapper cartDOMapper;

    @Override
    @Transactional
    public void addToCart(Integer itemId, Integer userId, Integer amount) {
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        if(itemModel.getStatus() == 2){
            throw new BusinessException(EmBusinessError.ITEM_NOT_SALE);
        }
        CartDO cartDO = cartDOMapper.selectByItemIdAndUserId(itemId,userId);
        if(cartDO == null ) {
            CartModel cartModel = new CartModel();
            cartModel.setItemId(itemId);
            cartModel.setUserId(userId);
            cartModel.setAmount(amount);
            cartModel.setPrice(itemModel.getPrice());
            cartModel.setTotalPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
            cartModel.setStatus(1);
            cartModel.setCreateTime(new Date());
            cartModel.setUpdateTime(new Date());
            CartDO cartDO1 = this.convertFromModel(cartModel);
            cartDOMapper.insertSelective(cartDO1);
        }else if(cartDO.getUpdateTime().before(itemModel.getUpdateTime())){
            cartDOMapper.invalidCartContent(cartDO.getId());
            CartModel cartModel = new CartModel();
            cartModel.setItemId(itemId);
            cartModel.setUserId(userId);
            cartModel.setAmount(amount);
            cartModel.setPrice(itemModel.getPrice());
            cartModel.setTotalPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
            cartModel.setStatus(1);
            cartModel.setCreateTime(new Date());
            cartModel.setUpdateTime(new Date());
            CartDO cartDO1 = this.convertFromModel(cartModel);
            cartDOMapper.insertSelective(cartDO1);
        }else {
            Integer newAmount = cartDO.getAmount() + amount;
            cartDO.setAmount(newAmount);
            cartDO.setTotalPrice(itemModel.getPrice().multiply(new BigDecimal(newAmount)).doubleValue());
            cartDO.setUpdateTime(new Date());
            cartDOMapper.updateByPrimaryKeySelective(cartDO);
        }
    }

    @Override
    @Transactional
    public void deleteFromCart(Integer id, Integer userId) {
        CartDO cartDO = cartDOMapper.selectByPrimaryKey(id);
        if(cartDO == null){
            throw new BusinessException(EmBusinessError.CART_NOT_EXIST);
        }
        if(cartDO.getUserId() != userId){
            throw new BusinessException(EmBusinessError.USER_STATUS_ERROR);
        }
        int result = cartDOMapper.deleteCartContent(id);
        if(result != 1){
            throw new BusinessException(EmBusinessError.CART_STATUS_ERROR);
        }
    }

    @Override
    @Transactional
    public CartModel updateCart(Integer id, Integer amount, Integer userId) {
        CartDO cartDO = cartDOMapper.selectByPrimaryKey(id);
        if(cartDO == null){
            throw new BusinessException(EmBusinessError.CART_NOT_EXIST);
        }
        if(cartDO.getUserId() != userId){
            throw new BusinessException(EmBusinessError.USER_STATUS_ERROR);
        }
        if(cartDO.getStatus() != 1){
            throw new BusinessException(EmBusinessError.CART_STATUS_ERROR);
        }
        ItemModel itemModel = itemService.getItemById(cartDO.getItemId());
        if(cartDO.getUpdateTime().before(itemModel.getUpdateTime())){
            cartDOMapper.invalidCartContent(cartDO.getId());
            cartDO.setStatus(3);
            CartModel cartModel = this.convertFromDO(cartDO);
            return cartModel;
        }
        cartDO.setAmount(amount);
        cartDO.setTotalPrice(cartDO.getPrice()*amount);
        cartDO.setUpdateTime(new Date());
        cartDOMapper.updateByPrimaryKeySelective(cartDO);
        CartModel cartModel = this.convertFromDO(cartDO);
        return cartModel;
    }

    @Override
    @Transactional
    public List<CartModel> list(Integer userId) {
        List<CartDO> cartDOList = cartDOMapper.selectByUserId(userId);
        if(cartDOList == null || cartDOList.size() == 0){
            return null;
        }
        List<CartModel> cartModelList = cartDOList.stream().map(cartDO -> {
            if(cartDO.getStatus() == 1){
                ItemModel itemModel = itemService.getItemById(cartDO.getItemId());
                if(cartDO.getUpdateTime().before(itemModel.getUpdateTime())){
                    cartDOMapper.invalidCartContent(cartDO.getId());
                    cartDO.setStatus(3);
                }
            }
            CartModel cartModel = this.convertFromDO(cartDO);
            return cartModel;
        }).collect(Collectors.toList());
        return cartModelList;
    }

    private CartDO convertFromModel(CartModel cartModel){
        if(cartModel == null){
            return null;
        }
        CartDO cartDO = new CartDO();
        BeanUtils.copyProperties(cartModel,cartDO);
        cartDO.setPrice(cartModel.getPrice().doubleValue());
        cartDO.setTotalPrice(cartModel.getTotalPrice().doubleValue());
        return cartDO;
    }

    private CartModel convertFromDO(CartDO cartDO){
        if(cartDO == null){
            return null;
        }
        CartModel cartModel = new CartModel();
        BeanUtils.copyProperties(cartDO,cartModel);
        cartModel.setPrice(new BigDecimal(cartDO.getPrice()));
        cartModel.setTotalPrice(new BigDecimal(cartDO.getTotalPrice()));
        return cartModel;
    }
}
