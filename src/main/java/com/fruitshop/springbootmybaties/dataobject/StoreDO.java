package com.fruitshop.springbootmybaties.dataobject;

import java.util.Date;

public class StoreDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column store_info.id
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column store_info.store_name
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    private String storeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column store_info.address
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column store_info.telphone
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    private String telphone;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column store_info.create_time
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column store_info.update_time
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column store_info.id
     *
     * @return the value of store_info.id
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column store_info.id
     *
     * @param id the value for store_info.id
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column store_info.store_name
     *
     * @return the value of store_info.store_name
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column store_info.store_name
     *
     * @param storeName the value for store_info.store_name
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column store_info.address
     *
     * @return the value of store_info.address
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column store_info.address
     *
     * @param address the value for store_info.address
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column store_info.telphone
     *
     * @return the value of store_info.telphone
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public String getTelphone() {
        return telphone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column store_info.telphone
     *
     * @param telphone the value for store_info.telphone
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public void setTelphone(String telphone) {
        this.telphone = telphone == null ? null : telphone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column store_info.create_time
     *
     * @return the value of store_info.create_time
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column store_info.create_time
     *
     * @param createTime the value for store_info.create_time
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column store_info.update_time
     *
     * @return the value of store_info.update_time
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column store_info.update_time
     *
     * @param updateTime the value for store_info.update_time
     *
     * @mbg.generated Mon Feb 10 18:46:43 CST 2020
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}