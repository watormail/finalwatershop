/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.etc.service.impl;

import com.etc.api.vo.NewBeeMallUserAddressVO;
import com.etc.common.NewBeeMallException;
import com.etc.common.ServiceResultEnum;
import com.etc.dao.UserAddressDao;
import com.etc.entity.MallUserAddress;
import com.etc.service.UserAddressService;
import com.etc.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressDao userAddressMapper;

    @Override
    public List<NewBeeMallUserAddressVO> getMyAddresses(Long userId) {
        List<MallUserAddress> myAddressList = userAddressMapper.findByUserId(userId);
        myAddressList.forEach(System.out::println);
        List<NewBeeMallUserAddressVO> newBeeMallUserAddressVOS = BeanUtil.copyList(myAddressList, NewBeeMallUserAddressVO.class);
        return newBeeMallUserAddressVOS;
    }

    @Override
    @Transactional
    public Boolean saveUserAddress(MallUserAddress mallUserAddress) {
        Date now = new Date();
        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            //添加默认地址，需要将原有的默认地址修改掉
            MallUserAddress defaultAddress = userAddressMapper.findByUserIdAndDefaultFlagAndIsDeleted(mallUserAddress.getUserId(),(byte)1,(byte)0);
            if (defaultAddress != null) {
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                userAddressMapper.save(defaultAddress);
            }
        }
        userAddressMapper.save(mallUserAddress);
        return true;
    }

    @Override
    public Boolean updateMallUserAddress(MallUserAddress mallUserAddress) {
        MallUserAddress tempAddress = getMallUserAddressById(mallUserAddress.getAddressId());
        Date now = new Date();
        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            //修改为默认地址，需要将原有的默认地址修改掉
            MallUserAddress defaultAddress = userAddressMapper.findByUserIdAndDefaultFlagAndIsDeleted(mallUserAddress.getUserId(),(byte)1,(byte)0);
            if (defaultAddress != null && !defaultAddress.getAddressId().equals(tempAddress)) {
                //存在默认地址且默认地址并不是当前修改的地址
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                userAddressMapper.save(defaultAddress);
            }
        }
        mallUserAddress.setUpdateTime(now);
        userAddressMapper.save(mallUserAddress);
        return true;
    }

    @Override
    public MallUserAddress getMallUserAddressById(Long addressId) {
        MallUserAddress mallUserAddress = userAddressMapper.findById(addressId).get();
        if (mallUserAddress == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return mallUserAddress;
    }

    @Override
    public MallUserAddress getMyDefaultAddressByUserId(Long userId) {
        return userAddressMapper.findByUserIdAndDefaultFlagAndIsDeleted(userId,(byte)1,(byte)0);
    }

    @Override
    public Boolean deleteById(Long addressId) {
        userAddressMapper.deleteById(addressId);
        return true;
    }
}
