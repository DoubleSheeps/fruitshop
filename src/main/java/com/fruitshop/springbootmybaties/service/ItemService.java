package com.fruitshop.springbootmybaties.service;

import com.fruitshop.springbootmybaties.service.model.ItemModel;

public interface ItemService {
    //通过id查询商品信息
    ItemModel getItemById(Integer id);

    //创建商品
    ItemModel createItem(ItemModel itemModel);
}
