package com.fruitshop.springbootmybaties.service.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;

public class UserModel {

    private Integer id;
    @NotBlank(message = "用户名不能为空")
    @Length(max = 8,message = "用户名长度不能大于8个字符")
    private String name;
    @NotBlank(message = "手机号不能为空")
    @Length(max = 11,min = 11,message = "手机号为11位数字")
    private String telphone;
    @NotBlank(message = "电子邮箱不能为空")
    @Email(message = "电子邮箱格式不正确")
    private String email;
    @NotNull(message = "性别不能为空")
    private Integer gender;
    @NotNull(message = "年龄不能为空")
    @Min(value = 0,message = "年龄不小于0岁")
    @Max(value = 150,message = "年龄不大于150岁")
    private Integer age;
    @NotBlank(message = "密码不能为空")
    private String encrptPassword;
    private Date createTime;
    private Date updateTime;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }
}
