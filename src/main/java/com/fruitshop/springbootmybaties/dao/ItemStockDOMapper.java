package com.fruitshop.springbootmybaties.dao;

import com.fruitshop.springbootmybaties.dataobject.ItemStockDO;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStockDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 04 22:14:52 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 04 22:14:52 CST 2020
     */
    int insert(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 04 22:14:52 CST 2020
     */
    int insertSelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 04 22:14:52 CST 2020
     */
    ItemStockDO selectByPrimaryKey(Integer id);

    /**
     * 自定义通过用户id查询
     * @param itemId 用户id
     * @return DO
     */
    ItemStockDO selectByItemId(Integer itemId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 04 22:14:52 CST 2020
     */
    int updateByPrimaryKeySelective(ItemStockDO record);

    int updateByItemIdSelective(ItemStockDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Tue Feb 04 22:14:52 CST 2020
     */
    int updateByPrimaryKey(ItemStockDO record);

    int decreaseStock(Integer itemId, Integer amount);

    int increaseStock(Integer itemId, Integer amount);
}