package com.fruitshop.springbootmybaties.dao;

import com.fruitshop.springbootmybaties.dataobject.CartDO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cart_info
     *
     * @mbg.generated Tue Feb 11 12:35:36 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cart_info
     *
     * @mbg.generated Tue Feb 11 12:35:36 CST 2020
     */
    int insert(CartDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cart_info
     *
     * @mbg.generated Tue Feb 11 12:35:36 CST 2020
     */
    int insertSelective(CartDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cart_info
     *
     * @mbg.generated Tue Feb 11 12:35:36 CST 2020
     */
    CartDO selectByPrimaryKey(Integer id);

    CartDO selectByItemIdAndUserId(Integer itemId, Integer userId);

    List<CartDO> selectByUserId(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cart_info
     *
     * @mbg.generated Tue Feb 11 12:35:36 CST 2020
     */
    int updateByPrimaryKeySelective(CartDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cart_info
     *
     * @mbg.generated Tue Feb 11 12:35:36 CST 2020
     */
    int updateByPrimaryKey(CartDO record);

    int deleteCartContent(Integer id);

    int invalidCartContent(Integer id);

    int updateAmount(CartDO record);
}