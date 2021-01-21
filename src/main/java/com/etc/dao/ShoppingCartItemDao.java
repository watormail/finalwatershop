/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.etc.dao;

import com.etc.entity.NewBeeMallOrder;
import com.etc.entity.NewBeeMallShoppingCartItem;
import com.etc.util.PageQueryUtil;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface ShoppingCartItemDao extends JpaSpecificationExecutor<NewBeeMallShoppingCartItem>, JpaRepository<NewBeeMallShoppingCartItem,Long>, Serializable {
//    int deleteByPrimaryKey(Long cartItemId);
//
//    int insert(NewBeeMallShoppingCartItem record);
//
//    int insertSelective(NewBeeMallShoppingCartItem record);
//
//    NewBeeMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    NewBeeMallShoppingCartItem findByUserIdAndGoodsId(Long newBeeMallUserId, Long goodsId);

    List<NewBeeMallShoppingCartItem> findByUserId(Long newBeeMallUserId);

    List<NewBeeMallShoppingCartItem> findByUserIdAndCartItemIdIn(Long newBeeMallUserId, List<Long> cartItemIds);


   // int deleteBatch(List<Long> ids);

    public default List<NewBeeMallShoppingCartItem> queryOrder(PageQueryUtil product) {
        return this.findAll(new Specification<NewBeeMallShoppingCartItem>() {
            @Override
            public Predicate toPredicate(Root<NewBeeMallShoppingCartItem> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if(product.get("userId")!=null&&!product.get("userId").equals("")){
                    Path<Long> path=root.get("userId");
                    list.add(criteriaBuilder.equal(path,product.get("userId")));
                }
                Path<Long> path=root.get("isDeleted");
                list.add(criteriaBuilder.equal(path,0));
                Predicate pp[]=new Predicate[list.size()];
                criteriaQuery.where(list.toArray(pp));
                return null;
            }
        });
    }
   // List<NewBeeMallShoppingCartItem> findMyNewBeeMallCartItems(PageQueryUtil pageUtil);

  //  int getTotalMyNewBeeMallCartItems(PageQueryUtil pageUtil);
}