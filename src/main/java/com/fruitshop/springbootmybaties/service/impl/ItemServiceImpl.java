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
import java.util.List;
import java.util.stream.Collectors;

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
        int result = itemDOMapper.insertSelective(itemDO);
        if(result != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"创建失败");
        }
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
        int result1 = itemStockDOMapper.insertSelective(itemStockDO);
        if(result1 != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"创建失败");
        }
        return itemModel;
    }

    @Override
    @Transactional
    public ItemModel updateItem(ItemModel itemModel) {
        ValidationResult validationResult = validator.validate(itemModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        ItemDO itemDO = this.convertItemDOFromModel(itemModel);
        int result = itemDOMapper.updateByPrimaryKeySelective(itemDO);
        if(result != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"更新失败");
        }
        ItemStockDO itemStockDO = this.convertItemStockDOFromModel(itemModel);
        int result1 = itemStockDOMapper.updateByItemIdSelective(itemStockDO);
        if(result1 != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR,"更新失败");
        }
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> getItemList(Integer sort) {
        List<ItemDO> itemDOList = itemDOMapper.selectBySort(sort);
        if(itemDOList == null || itemDOList.size() == 0){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST,"未找到销售中的商品");
        }
        List<ItemModel> itemModelList = this.convertFromDOList(itemDOList);
        return itemModelList;
    }

    @Override
    public List<ItemModel> searchItem(String keyWord) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('%');
        stringBuilder.append(keyWord);
        stringBuilder.append('%');
        List<ItemDO> itemDOList = itemDOMapper.selectByKeyWord(stringBuilder.toString());
        if(itemDOList == null || itemDOList.size() == 0){
            throw new BusinessException(EmBusinessError.ITEM_NOT_EXIST,"未找到相关商品");
        }
        List<ItemModel> itemModelList = this.convertFromDOList(itemDOList);
        return itemModelList;
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

    private List<ItemModel> convertFromDOList(List<ItemDO> itemDOList){
        List<ItemModel> itemModelList = itemDOList.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            if(itemStockDO == null){
                return null;
            }
            ItemModel itemModel = this.convertFromDataObject(itemDO,itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return itemModelList;
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
