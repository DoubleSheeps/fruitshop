package com.fruitshop.springbootmybaties.service.model;

import java.math.BigDecimal;

public class ItemModel {
    private Integer id;
    private String title;
    private String description;

    //分类
    private Integer sort;
    private BigDecimal price;
    private Integer stock;
    private Integer sales;
    private String storeName;
    private String createTime;
    private String updateTime;
}
