package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.controller.viewobject.ItemVO;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = this.convertFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    private ItemVO convertFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }
}
