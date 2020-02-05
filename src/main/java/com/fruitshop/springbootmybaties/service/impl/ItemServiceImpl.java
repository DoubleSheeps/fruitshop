package com.fruitshop.springbootmybaties.service.impl;

import com.fruitshop.springbootmybaties.dao.ItemDOMapper;
import com.fruitshop.springbootmybaties.dao.ItemStockDOMapper;
import com.fruitshop.springbootmybaties.dataobject.ItemDO;
import com.fruitshop.springbootmybaties.dataobject.ItemStockDO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemDOMapper itemDOMapper;
    @Autowired
    private ItemStockDOMapper itemStockDOMapper;

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDo = itemDOMapper.selectByPrimaryKey(id);
        if(itemDo == null){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST);
        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id);
        if(itemStockDO == null){
            return null;
        }
        ItemModel itemModel = this.convertFormDataObject(itemDo,itemStockDO);
        return itemModel;
    }

    private ItemModel convertFormDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        if(itemDO == null || itemStockDO == null){
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
