/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.etc.service.impl;

import com.etc.api.param.MallUserUpdateParam;
import com.etc.common.Constants;
import com.etc.common.NewBeeMallException;
import com.etc.common.ServiceResultEnum;
import com.etc.dao.UserDao;
import com.etc.dao.UserTokenDao;
import com.etc.entity.MallUser;
import com.etc.entity.MallUserToken;

import com.etc.service.UserService;
import com.etc.util.MD5Util;
import com.etc.util.NumberUtil;
import com.etc.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao mallUserMapper;
    @Autowired
    private UserTokenDao newBeeMallUserTokenMapper;

    @Override
    public String register(String loginName, String password) {
        if (mallUserMapper.findByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        MallUser registerUser = new MallUser();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        registerUser.setIntroduceSign(Constants.USER_INTRO);
        String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
        registerUser.setPasswordMd5(passwordMD5);
        registerUser.setIsDeleted((byte)0);
        registerUser.setLockedFlag((byte)0);
        if (mallUserMapper.save(registerUser) !=null) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String passwordMD5) {
        MallUser user = mallUserMapper.findByLoginNameAndPasswordMd5(loginName, passwordMD5);
        if (user != null) {
            if (user.getLockedFlag() == 1) {
                return ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult();
            }
            //登录后即执行修改token的操作
            String token = getNewToken(System.currentTimeMillis() + "", user.getUserId());
            MallUserToken mallUserToken = newBeeMallUserTokenMapper.findById(user.getUserId()).get();
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
            if (mallUserToken == null) {
                mallUserToken = new MallUserToken();
                mallUserToken.setUserId(user.getUserId());
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //新增一条token数据
                if (newBeeMallUserTokenMapper.save(mallUserToken) !=null) {
                    //新增成功后返回
                    return token;
                }
            } else {
                mallUserToken.setToken(token);
                mallUserToken.setUpdateTime(now);
                mallUserToken.setExpireTime(expireTime);
                //更新
                if (newBeeMallUserTokenMapper.save(mallUserToken) !=null) {
                    //修改成功后返回
                    return token;
                }
            }

        }
        return ServiceResultEnum.LOGIN_ERROR.getResult();
    }

    /**
     * 获取token值
     *
     * @param timeStr
     * @param userId
     * @return
     */
    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }

    @Override
    public Boolean updateUserInfo(MallUserUpdateParam mallUser, Long userId) {
        MallUser user = mallUserMapper.findById(userId).get();
        if (user == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        user.setNickName(mallUser.getNickName());
        //user.setPasswordMd5(mallUser.getPasswordMd5());
        //若密码为空字符，则表明用户不打算修改密码，使用原密码保存
        if (!MD5Util.MD5Encode("", "UTF-8").equals(mallUser.getPasswordMd5())){
            user.setPasswordMd5(mallUser.getPasswordMd5());
        }
        user.setIntroduceSign(mallUser.getIntroduceSign());
        if (mallUserMapper.save(user) !=null) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean logout(Long userId) {
         newBeeMallUserTokenMapper.deleteById(userId);
         return true;
    }
}
