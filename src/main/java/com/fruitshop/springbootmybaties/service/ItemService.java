package com.fruitshop.springbootmybaties.service;

import com.fruitshop.springbootmybaties.service.model.ItemModel;

public interface ItemService {
    ItemModel getItemById(Integer id);
}
