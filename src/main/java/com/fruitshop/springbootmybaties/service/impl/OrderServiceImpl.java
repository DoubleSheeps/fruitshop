package com.fruitshop.springbootmybaties.service.impl;

import com.fruitshop.springbootmybaties.dao.LogisticsDOMapper;
import com.fruitshop.springbootmybaties.dao.OrderDOMapper;
import com.fruitshop.springbootmybaties.dao.SequenceDOMapper;
import com.fruitshop.springbootmybaties.dataobject.LogisticsDO;
import com.fruitshop.springbootmybaties.dataobject.OrderDO;
import com.fruitshop.springbootmybaties.dataobject.SequenceDO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.OrderService;
import com.fruitshop.springbootmybaties.service.model.ItemModel;
import com.fruitshop.springbootmybaties.service.model.LogisticsModel;
import com.fruitshop.springbootmybaties.service.model.OrderModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDOMapper orderDOMapper;
    @Autowired
    private SequenceDOMapper sequenceDOMapper;
    @Autowired
    private ItemService itemService;
    @Autowired
    private LogisticsDOMapper logisticsDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer itemId, Integer userId, Integer amount) {
        ItemModel itemModel = itemService.getItemById(itemId);
        if(itemModel == null){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
//        UserModel userModel = userService.getUserById(userId);
//        if(userModel == null){
//            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
//        }
        if(amount <= 0 || amount >= 99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR.setErrMsg("数量信息不正确"));
        }

        //减少库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.ITEM_NOT_ENOUGH);
        }

        OrderModel orderModel = new OrderModel();
        //生成订单号
        orderModel.setId(this.generateOrderNo());
        orderModel.setItemId(itemId);
        orderModel.setUserId(userId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
        orderModel.setCreateTime(new Date());
        orderModel.setStatus(1);
        OrderDO orderDO = this.convertFromOrderModer(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //增加销量
        itemService.increaseSales(itemId,amount);
        //返回前端

        return orderModel;
    }

    @Override
    public List<OrderModel> getOrders(Integer userId) {
//        UserModel userModel = userService.getUserById(userId);
//        if(userModel == null){
//            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
//        }
        List<OrderDO> orderDOList = orderDOMapper.selectByUserId(userId);
        List<OrderModel> orderModelList = orderDOList.stream().map(orderDO -> {
            OrderModel orderModel = this.convertFromDataObject(orderDO);
            return orderModel;
        }).collect(Collectors.toList());
        return orderModelList;
    }

    @Override
    @Transactional
    public LogisticsModel completeOrder(String id, Integer userId, Integer paymentMethod, String address) {
        OrderDO orderDO = orderDOMapper.selectByPrimaryKey(id);
        if(orderDO == null){
            throw new BusinessException(EmBusinessError.ORDER_NOT_EXIST);
        }
        if(orderDO.getUserId() != userId){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"登录信息异常");
        }
        orderDO.setPaymentMethod(paymentMethod);
        orderDO.setStatus(2);
        int result = orderDOMapper.updateByPrimaryKeySelective(orderDO);
        if(result != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"更新订单失败");
        }
        LogisticsDO logisticsDO = new LogisticsDO();
        logisticsDO.setId(this.generateLogisticsNo());
        logisticsDO.setOrderId(id);
        logisticsDO.setAddress(address);
        logisticsDO.setCreateTime(new Date());
        int result1 = logisticsDOMapper.insertSelective(logisticsDO);
        if(result1 != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"创建物流失败");
        }
        return this.convertFromDataObject(logisticsDO);
    }

    @Override
    @Transactional
    public void cancelOrder(String id, Integer userId) {
        OrderDO orderDO = orderDOMapper.selectByPrimaryKey(id);
        if(orderDO == null){
            throw new BusinessException(EmBusinessError.ORDER_NOT_EXIST);
        }
        if(orderDO.getUserId() != userId){
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN,"登录信息异常");
        }
        if(orderDO.getStatus() != 1){
            throw new BusinessException(EmBusinessError.ORDER_STATUS_ERROR);
        }
        orderDO.setStatus(3);
        orderDOMapper.updateByPrimaryKeySelective(orderDO);
        itemService.increaseStock(orderDO.getItemId(),orderDO.getAmount());
        Boolean result = itemService.decreaseSales(orderDO.getItemId(),orderDO.getAmount());
        if(!result){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"销量异常");
        }
    }

    private OrderModel convertFromDataObject(OrderDO orderDO){
        if(orderDO == null){
            return null;
        }
        OrderModel orderModel = new OrderModel();
        BeanUtils.copyProperties(orderDO,orderModel);
        orderModel.setItemPrice(new BigDecimal(orderDO.getItemPrice()));
        orderModel.setOrderPrice(new BigDecimal(orderDO.getOrderPrice()));
        return orderModel;
    }

    private LogisticsModel convertFromDataObject(LogisticsDO logisticsDO){
        if(logisticsDO == null){
            return null;
        }
        LogisticsModel logisticsModel = new LogisticsModel();
        BeanUtils.copyProperties(logisticsDO,logisticsModel);
        return logisticsModel;
    }


    private OrderDO convertFromOrderModer(OrderModel orderModel) {
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }

    //事物内部调用的方法的标签也能事物回滚
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNo(){
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");

        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceStr = String.valueOf(sequence);
        for(int i = 0; i < 6 - sequenceStr.length(); i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);


        return stringBuilder.toString();
    }

    //事物内部调用的方法的标签也能事物回滚
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateLogisticsNo(){
        //订单号有16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间6位为自增序列
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("logistics_info");

        sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String sequenceStr = String.valueOf(sequence);
        for(int i = 0; i < 6 - sequenceStr.length(); i++){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);


        return stringBuilder.toString();
    }
}
