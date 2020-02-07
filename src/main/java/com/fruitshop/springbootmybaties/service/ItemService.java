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
}
