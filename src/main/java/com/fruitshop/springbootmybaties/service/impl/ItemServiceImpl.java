package com.fruitshop.springbootmybaties.service.impl;

import com.fruitshop.springbootmybaties.dao.ItemDOMapper;
import com.fruitshop.springbootmybaties.dao.ItemStockDOMapper;
import com.fruitshop.springbootmybaties.dataobject.ItemDO;
import com.fruitshop.springbootmybaties.dataobject.ItemStockDO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.model.ItemModel;
import com.fruitshop.springbootmybaties.validator.ValidationResult;
import com.fruitshop.springbootmybaties.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ValidatorImpl validator;

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
        if(itemDo.getStatus() == 2){
            throw new BusinessException(EmBusinessError.ITEM_NOT_SALE);
        }
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(id);
        if(itemStockDO == null){
            return null;
        }
        ItemModel itemModel = this.convertFromDataObject(itemDo,itemStockDO);
        return itemModel;
    }

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) {
        ValidationResult validationResult = validator.validate(itemModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        ItemDO itemDO = this.convertItemDOFromModel(itemModel);
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        return itemModel;
    }

    private ItemModel convertFromDataObject(ItemDO itemDO,ItemStockDO itemStockDO){
        if(itemDO == null || itemStockDO == null){
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }

    private ItemDO convertItemDOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    private ItemStockDO convertItemStockDOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }
}
