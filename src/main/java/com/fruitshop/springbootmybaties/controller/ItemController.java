package com.fruitshop.springbootmybaties.controller;

import com.fruitshop.springbootmybaties.controller.viewobject.ItemVO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.response.CommonReturnType;
import com.fruitshop.springbootmybaties.service.ItemService;
import com.fruitshop.springbootmybaties.service.model.ItemModel;
import com.fruitshop.springbootmybaties.service.model.StoreModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/item")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getItem(@RequestParam(name = "id")Integer id){
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = this.convertFromModel(itemModel);
        return CommonReturnType.create(itemVO);
    }

    @RequestMapping(value = "/create",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType createItem(@RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "sort")Integer sort,
                                       @RequestParam(name = "imgUrl") String imgUrl,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock){
        Boolean isStoreLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if(isStoreLogin == null || !isStoreLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN,"商铺登录信息失效");
        }
        ItemModel itemModel= new ItemModel();
        StoreModel storeModel = (StoreModel) httpServletRequest.getSession().getAttribute("LOGIN_STORE");
        itemModel.setStoreName(storeModel.getStoreName());
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setSort(sort);
        itemModel.setSales(0);
        itemModel.setImgUrl(imgUrl);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setStatus(1);
        itemModel.setCreateTime(new Date());
        itemModel.setUpdateTime(new Date());
        ItemModel returnItemModel = itemService.createItem(itemModel);
        ItemVO itemVO = this.convertFromModel(returnItemModel);
        return CommonReturnType.create(itemVO);
    }

    @RequestMapping(value = "/update",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType updateItem(@RequestParam(name = "id") Integer id,
                                       @RequestParam(name = "title")String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "sort")Integer sort,
                                       @RequestParam(name = "imgUrl") String imgUrl,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock){
        Boolean isStoreLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_STORE_LOGIN");
        if(isStoreLogin == null || !isStoreLogin.booleanValue()){
            throw new BusinessException(EmBusinessError.STORE_NOT_LOGIN,"商铺登录信息失效");
        }
        ItemModel itemModel= new ItemModel();
        StoreModel storeModel = (StoreModel) httpServletRequest.getSession().getAttribute("LOGIN_STORE");
        itemModel.setStoreName(storeModel.getStoreName());
        itemModel.setId(id);
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setSort(sort);
        itemModel.setImgUrl(imgUrl);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setStatus(1);
        itemModel.setUpdateTime(new Date());
        ItemModel returnItemModel = itemService.updateItem(itemModel);
        ItemVO itemVO = this.convertFromModel(returnItemModel);
        return CommonReturnType.create(itemVO);
    }


    @RequestMapping(value = "/list",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType getItemList(@RequestParam(name = "sort") Integer sort){
        List<ItemModel> itemModelList = itemService.getItemList(sort);
        List<ItemVO> itemVOList = this.convertFromModelList(itemModelList);
        return CommonReturnType.create(itemVOList);
    }

    @RequestMapping(value = "search",method = {RequestMethod.POST},consumes = {BaseController.CONTENT_TYPE_FORMED})
    public CommonReturnType searchByKeyWord(@RequestParam(name = "keyWord") String keyWord){
        List<ItemModel> itemModelList = itemService.searchItem(keyWord);
        List<ItemVO> itemVOList = this.convertFromModelList(itemModelList);
        return CommonReturnType.create(itemVOList);
    }

    private ItemVO convertFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        return itemVO;
    }

    private List<ItemVO> convertFromModelList(List<ItemModel> itemModelList){
        if(itemModelList == null || itemModelList.size() == 0){
            return null;
        }
        List<ItemVO> itemVOList = itemModelList.stream().map(itemModel -> {
            ItemVO itemVO = this.convertFromModel(itemModel);
            return itemVO;
        }).collect(Collectors.toList());
        return itemVOList;
    }
}
