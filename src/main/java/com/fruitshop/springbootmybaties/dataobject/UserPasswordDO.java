package com.fruitshop.springbootmybaties.dataobject;

import java.util.Date;

public class UserPasswordDO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_password.id
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_password.user_id
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    private Integer userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_password.encrpt_password
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    private String encrptPassword;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_password.create_time
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_password.update_time
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.id
     *
     * @return the value of user_password.id
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.id
     *
     * @param id the value for user_password.id
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.user_id
     *
     * @return the value of user_password.user_id
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.user_id
     *
     * @param userId the value for user_password.user_id
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.encrpt_password
     *
     * @return the value of user_password.encrpt_password
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public String getEncrptPassword() {
        return encrptPassword;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.encrpt_password
     *
     * @param encrptPassword the value for user_password.encrpt_password
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword == null ? null : encrptPassword.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.create_time
     *
     * @return the value of user_password.create_time
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.create_time
     *
     * @param createTime the value for user_password.create_time
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_password.update_time
     *
     * @return the value of user_password.update_time
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_password.update_time
     *
     * @param updateTime the value for user_password.update_time
     *
     * @mbg.generated Sun Feb 09 21:28:54 CST 2020
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}