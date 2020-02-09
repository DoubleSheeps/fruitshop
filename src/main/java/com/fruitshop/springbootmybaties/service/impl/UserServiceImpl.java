package com.fruitshop.springbootmybaties.service.impl;

import com.fruitshop.springbootmybaties.dao.UserDOMapper;
import com.fruitshop.springbootmybaties.dao.UserPasswordDOMapper;
import com.fruitshop.springbootmybaties.dataobject.UserDO;
import com.fruitshop.springbootmybaties.dataobject.UserPasswordDO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.service.UserService;
import com.fruitshop.springbootmybaties.service.model.UserModel;
import com.fruitshop.springbootmybaties.validator.ValidationResult;
import com.fruitshop.springbootmybaties.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){
            return null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        return convertFromDataObject(userDO,userPasswordDO);
    }

    @Override
    public UserModel getUserByTelphone(String telphone) {
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = this.convertFromDataObject(userDO,userPasswordDO);
        return userModel;
    }

    @Override
    public Boolean isRegisteredByTelphone(String telphone) {
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Boolean isRegisteredByEmail(String email) {
        UserDO userDO = userDOMapper.selectByEmail(email);
        if(userDO != null){
            return true;
        }else {
            return false;
        }
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

        ValidationResult validationResult = validator.validate(userModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }

        UserDO userDO = convertFromModel(userModel);



        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已注册");
        }

        userModel.setId(userDO.getId());

        UserPasswordDO userPasswordDO= convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    @Transactional
    public UserModel updateUser(UserModel userModel) {
        UserDO userDO = this.convertFromModel(userModel);
        int num = userDOMapper.updateByPrimaryKeySelective(userDO);
        if(num != 1){
            throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
        }
        return userModel;
    }

    @Override
    @Transactional
    public void changePassword(UserModel userModel) {
        UserPasswordDO userPasswordDO = this.convertPasswordFromModel(userModel);
        userPasswordDOMapper.updateByUserIdSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLoginByTelphone(String telphone, String encrptPassword) {
        //通过手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);
        //比对加密密码是否匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public UserModel validateLoginByEmail(String email, String encrptPassword) {
        //通过手机获取用户信息
        UserDO userDO = userDOMapper.selectByEmail(email);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);
        //比对加密密码是否匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private  UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptPassword());
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDO.setCreateTime(userModel.getCreateTime());
        userPasswordDO.setUpdateTime(userModel.getUpdateTime());
        return userPasswordDO;
    }

    //实现model->dataobject方法
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO){
        if(userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if(userPasswordDO != null){
            userModel.setEncrptPassword(userPasswordDO.getEncrptPassword());
        }
        return userModel;
    }
}
