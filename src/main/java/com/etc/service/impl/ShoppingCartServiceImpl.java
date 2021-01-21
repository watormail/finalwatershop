/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.etc.service.impl;

import com.etc.api.param.SaveCartItemParam;
import com.etc.api.param.UpdateCartItemParam;
import com.etc.common.Constants;
import com.etc.common.NewBeeMallException;
import com.etc.common.ServiceResultEnum;
import com.etc.api.vo.NewBeeMallShoppingCartItemVO;
import com.etc.dao.GoodsDao;
import com.etc.dao.ShoppingCartItemDao;
import com.etc.entity.NewBeeMallGoods;
import com.etc.entity.NewBeeMallShoppingCartItem;
import com.etc.service.ShoppingCartService;
import com.etc.util.BeanUtil;
import com.etc.util.PageQueryUtil;
import com.etc.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartItemDao newBeeMallShoppingCartItemMapper;

    @Autowired
    private GoodsDao newBeeMallGoodsMapper;

    @Override
    public String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        NewBeeMallShoppingCartItem temp = newBeeMallShoppingCartItemMapper.findByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            //已存在则修改该记录
            NewBeeMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }
        NewBeeMallGoods newBeeMallGoods = newBeeMallGoodsMapper.findById(saveCartItemParam.getGoodsId()).get();
        //商品为空
        if (newBeeMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = newBeeMallShoppingCartItemMapper.findByUserId(userId).size();
        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }        //超出单个商品的最大数量
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //超出最大数量
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }
        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
        BeanUtil.copyProperties(saveCartItemParam, newBeeMallShoppingCartItem);
        newBeeMallShoppingCartItem.setUserId(userId);
        newBeeMallShoppingCartItem.setIsDeleted((byte)0);
        newBeeMallShoppingCartItem.setCreateTime(new Date());
        //保存记录
        if (newBeeMallShoppingCartItemMapper.save(newBeeMallShoppingCartItem) !=null) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        NewBeeMallShoppingCartItem newBeeMallShoppingCartItemUpdate = newBeeMallShoppingCartItemMapper.findById(updateCartItemParam.getCartItemId()).get();
        if (newBeeMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!newBeeMallShoppingCartItemUpdate.getUserId().equals(userId)) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        //超出单个商品的最大数量
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        newBeeMallShoppingCartItemUpdate.setGoodsCount(updateCartItemParam.getGoodsCount());
        newBeeMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //修改记录
        if (newBeeMallShoppingCartItemMapper.save(newBeeMallShoppingCartItemUpdate) !=null) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public NewBeeMallShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = newBeeMallShoppingCartItemMapper.findById(newBeeMallShoppingCartItemId).get();
        if (newBeeMallShoppingCartItem == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return newBeeMallShoppingCartItem;
    }

    @Override
    public Boolean deleteById(Long newBeeMallShoppingCartItemId) {
        NewBeeMallShoppingCartItem shoppingCartItem=newBeeMallShoppingCartItemMapper.findById(newBeeMallShoppingCartItemId).get();
        shoppingCartItem.setIsDeleted((byte)1);
        newBeeMallShoppingCartItemMapper.save(shoppingCartItem) ;
        return true;
    }

    @Override
    public List<NewBeeMallShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
        List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.findByUserId(newBeeMallUserId);
        List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems1=new ArrayList<>();
        for(int i=0;i<Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER;i++){
            newBeeMallShoppingCartItems1.add(newBeeMallShoppingCartItems.get(i));
        }

        return getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems1);
    }

    @Override
    public List<NewBeeMallShoppingCartItemVO> getCartItemsForSettle(List<Long> cartItemIds, Long newBeeMallUserId) {
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
        if (CollectionUtils.isEmpty(cartItemIds)) {
            NewBeeMallException.fail("购物项不能为空");
        }
        List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.findByUserIdAndCartItemIdIn(newBeeMallUserId, cartItemIds);
        if (CollectionUtils.isEmpty(newBeeMallShoppingCartItems)) {
            NewBeeMallException.fail("购物项不能为空");
        }
        if (newBeeMallShoppingCartItems.size() != cartItemIds.size()) {
            NewBeeMallException.fail("参数异常");
        }
        return getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems);
    }

    /**
     * 数据转换
     *
     * @param newBeeMallShoppingCartItemVOS
     * @param newBeeMallShoppingCartItems
     * @return
     */
    private List<NewBeeMallShoppingCartItemVO> getNewBeeMallShoppingCartItemVOS(List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS, List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems) {
        if (!CollectionUtils.isEmpty(newBeeMallShoppingCartItems)) {
            //查询商品信息并做数据转换
            List<Long> newBeeMallGoodsIds = newBeeMallShoppingCartItems.stream().map(NewBeeMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<NewBeeMallGoods> newBeeMallGoods = new ArrayList<>();
            for(int i=0;i<newBeeMallGoodsIds.size();i++){
                newBeeMallGoods.add(newBeeMallGoodsMapper.findById(newBeeMallGoodsIds.get(i)).get());
            }
            Map<Long, NewBeeMallGoods> newBeeMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(newBeeMallGoods)) {
                newBeeMallGoodsMap = newBeeMallGoods.stream().collect(Collectors.toMap(NewBeeMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (NewBeeMallShoppingCartItem newBeeMallShoppingCartItem : newBeeMallShoppingCartItems) {
                NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO = new NewBeeMallShoppingCartItemVO();
                BeanUtil.copyProperties(newBeeMallShoppingCartItem, newBeeMallShoppingCartItemVO);
                if (newBeeMallGoodsMap.containsKey(newBeeMallShoppingCartItem.getGoodsId())) {
                    NewBeeMallGoods newBeeMallGoodsTemp = newBeeMallGoodsMap.get(newBeeMallShoppingCartItem.getGoodsId());
                    newBeeMallShoppingCartItemVO.setGoodsCoverImg(newBeeMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = newBeeMallGoodsTemp.getGoodsName();
                    // 字符串过长导致文字超出的问题
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    newBeeMallShoppingCartItemVO.setGoodsName(goodsName);
                    newBeeMallShoppingCartItemVO.setSellingPrice(newBeeMallGoodsTemp.getSellingPrice());
                    newBeeMallShoppingCartItemVOS.add(newBeeMallShoppingCartItemVO);
                }
            }
        }
        return newBeeMallShoppingCartItemVOS;
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtil pageUtil) {
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
        List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.queryOrder(pageUtil);
        int total = newBeeMallShoppingCartItems.size();
        PageResult pageResult = new PageResult(getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItemVOS, newBeeMallShoppingCartItems), total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
