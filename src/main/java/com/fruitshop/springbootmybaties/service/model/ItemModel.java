package com.fruitshop.springbootmybaties.service.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class ItemModel {
    private Integer id;
    @NotBlank(message = "商品名不能为空")
    private String title;
    @NotBlank(message = "商品描述不能为空")
    private String description;

    //分类 1水果，2水产，3肉类，4鸡蛋，5蔬菜，6速冻产品
    @Max(value = 6,message = "种类必须在1~6之间")
    @Min(value = 1,message = "种类必须在1~6之间")
    private Integer sort;
    //销售状态 1销售中，2已下架
    @Max(value = 2,message = "状态必须为1或2")
    @Min(value = 1,message = "状态必须为1或2")
    private Integer status;
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;
    @Min(value = 0,message = "商品库存不能为负数")
    @NotNull(message = "商品库存不能为空")
    private Integer stock;
    private Integer sales;
    @NotBlank(message = "商铺名不能为空")
    private String storeName;
    private Date createTime;
    private Date updateTime;
    @NotBlank(message = "商品图片不能为空")
    private String imgUrl;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
