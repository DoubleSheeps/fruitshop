package com.fruitshop.springbootmybaties.service;

import com.fruitshop.springbootmybaties.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //商品详情
    ItemModel getItemById(Integer id);

    //创建商品
    ItemModel createItem(ItemModel itemModel);

    //更新商品
    ItemModel updateItem(ItemModel itemModel);

    //获取商品列表
    List<ItemModel> getItemList(Integer sort);

    //查找商品
    List<ItemModel> searchItem(String keyWord);

    //减库存
    Boolean decreaseStock(Integer itemId, Integer amount);

    //返回库存
    void increaseStock(Integer itemId, Integer amount);

    //增销量
    void increaseSales(Integer itemId,Integer amount);

    Boolean decreaseSales(Integer itemId,Integer amount);
}
