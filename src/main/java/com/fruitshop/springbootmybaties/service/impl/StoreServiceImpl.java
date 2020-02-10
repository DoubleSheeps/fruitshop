package com.fruitshop.springbootmybaties.service.impl;

import com.fruitshop.springbootmybaties.dao.StoreDOMapper;
import com.fruitshop.springbootmybaties.dao.StorePasswordDOMapper;
import com.fruitshop.springbootmybaties.dataobject.StoreDO;
import com.fruitshop.springbootmybaties.dataobject.StorePasswordDO;
import com.fruitshop.springbootmybaties.error.BusinessException;
import com.fruitshop.springbootmybaties.error.EmBusinessError;
import com.fruitshop.springbootmybaties.service.StoreService;
import com.fruitshop.springbootmybaties.service.model.StoreModel;
import com.fruitshop.springbootmybaties.validator.ValidationResult;
import com.fruitshop.springbootmybaties.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {
    @Autowired
    private StoreDOMapper storeDOMapper;
    @Autowired
    private StorePasswordDOMapper storePasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;

    @Override
    public StoreModel getStoreByTelphone(String telphone) {
        return null;
    }

    @Override
    public Boolean isRegisteredByTelphone(String telphone) {
        StoreDO storeDO = storeDOMapper.selectByTelphone(telphone);
        if(storeDO == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Boolean isRegisterByStoreName(String storeName) {
        StoreDO storeDO = storeDOMapper.selectByStoreName(storeName);
        if(storeDO == null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void register(StoreModel storeModel) {
        if(storeModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult validationResult = validator.validate(storeModel);
        if(validationResult.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,validationResult.getErrMsg());
        }
        StoreDO storeDO = this.convertStoreDOFromModel(storeModel);
        try{
            storeDOMapper.insertSelective(storeDO);
        }catch (DuplicateKeyException e){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号已注册");
        }
        storeModel.setId(storeDO.getId());
        StorePasswordDO storePasswordDO = this.convertStorePasswordDOFromModel(storeModel);
        storePasswordDOMapper.insertSelective(storePasswordDO);
    }

    @Override
    public StoreModel validateLogin(String telphone, String encrptPassword) {
        StoreDO storeDO = storeDOMapper.selectByTelphone(telphone);
        if(storeDO == null){
            throw new BusinessException(EmBusinessError.STORE_NOT_EXIST);
        }
        StorePasswordDO storePasswordDO = storePasswordDOMapper.selectByStoreId(storeDO.getId());
        if(!StringUtils.equals(storePasswordDO.getEncrptPassword(),encrptPassword)){
            throw new BusinessException(EmBusinessError.STORE_LOGIN_FAIL);
        }
        StoreModel storeModel = this.convertFromDO(storeDO,storePasswordDO);
        return storeModel;
    }
    private StoreModel convertFromDO(StoreDO storeDO,StorePasswordDO storePasswordDO){
        if(storeDO == null || storePasswordDO == null){
            return null;
        }
        StoreModel storeModel = new StoreModel();
        BeanUtils.copyProperties(storeDO,storeModel);
        storeModel.setEncrptPassword(storePasswordDO.getEncrptPassword());
        return storeModel;
    }
    private StoreDO convertStoreDOFromModel(StoreModel storeModel){
        if(storeModel == null){
            return null;
        }
        StoreDO storeDO = new StoreDO();
        BeanUtils.copyProperties(storeModel,storeDO);
        return storeDO;
    }
    private StorePasswordDO convertStorePasswordDOFromModel(StoreModel storeModel){
        if(storeModel == null){
            return null;
        }
        StorePasswordDO storePasswordDO = new StorePasswordDO();
        storePasswordDO.setStoreId(storeModel.getId());
        storePasswordDO.setEncrptPassword(storeModel.getEncrptPassword());
        storePasswordDO.setCreateTime(storeModel.getCreateTime());
        storePasswordDO.setUpdateTime(storeModel.getUpdateTime());
        return storePasswordDO;
    }
}
