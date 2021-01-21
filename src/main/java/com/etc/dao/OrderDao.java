/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.etc.dao;

import com.etc.entity.NewBeeMallGoods;
import com.etc.entity.NewBeeMallOrder;
import com.etc.entity.NewBeeMallOrderAddress;
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

public interface OrderDao extends JpaSpecificationExecutor<NewBeeMallOrder>, JpaRepository<NewBeeMallOrder,Long>, Serializable {
//    int deleteByPrimaryKey(Long orderId);
//
//    int insert(NewBeeMallOrder record);
//
//    int insertSelective(NewBeeMallOrder record);
//
//    NewBeeMallOrder selectByPrimaryKey(Long orderId);
//
    NewBeeMallOrder findByOrOrderNo(String orderNo);

//    int updateByPrimaryKeySelective(NewBeeMallOrder record);
//
//    int updateByPrimaryKey(NewBeeMallOrder record);

    public default List<NewBeeMallOrder> queryOrder(PageQueryUtil product) {
        return this.findAll(new Specification<NewBeeMallOrder>() {
            @Override
            public Predicate toPredicate(Root<NewBeeMallOrder> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if(product.get("orderNo")!=null&&!product.get("orderNo").equals("")){
                    Path<Long> path=root.get("orderNo");
                    list.add(criteriaBuilder.equal(path,product.get("orderNo")));
                }
                if(product.get("userId")!=null&&!product.get("userId").equals("")){
                    Path<Long> path=root.get("userId");
                    list.add(criteriaBuilder.equal(path,product.get("userId")));
                }
                if(product.get("payType")!=null){
                    Path<Byte> path=root.get("payType");
                    list.add(criteriaBuilder.equal(path,product.get("payType")));
                }
                if(product.get("orderStatus")!=null){
                    Path<Byte> path=root.get("orderStatus");
                    list.add(criteriaBuilder.equal(path,product.get("orderStatus")));
                }
                if(product.get("isDeleted")!=null){
                    Path<Byte> path=root.get("isDeleted");
                    list.add(criteriaBuilder.equal(path,product.get("isDeleted")));
                }
                Predicate pp[]=new Predicate[list.size()];
                criteriaQuery.where(list.toArray(pp));
                return null;
            }
        });
    }
//    List<NewBeeMallOrder> findNewBeeMallOrderList(PageQueryUtil pageUtil);
//
//    int getTotalNewBeeMallOrders(PageQueryUtil pageUtil);

 //   List<NewBeeMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    //int checkOut(@Param("orderIds") List<Long> orderIds);

  //  int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

   // int checkDone(@Param("orderIds") List<Long> asList);
}