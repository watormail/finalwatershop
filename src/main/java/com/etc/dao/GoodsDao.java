/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本软件已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package com.etc.dao;

import com.etc.entity.Carousel;
import com.etc.entity.GoodsCategory;
import com.etc.entity.NewBeeMallGoods;
import com.etc.entity.StockNumDTO;
import com.etc.util.PageQueryUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;


import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface GoodsDao extends JpaSpecificationExecutor<NewBeeMallGoods>, JpaRepository<NewBeeMallGoods,Long>, Serializable {

    public default List<NewBeeMallGoods> query(PageQueryUtil product) {
        return this.findAll(new Specification<NewBeeMallGoods>() {
            @Override
            public Predicate toPredicate(Root<NewBeeMallGoods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if(product.get("keyword")!=null&&!product.get("keyword").equals("")){
                    Path<String> path=root.get("goodsName");
                    list.add(criteriaBuilder.like(path,"%"+product.get("keyword")+"%"));
                }
                if(product.get("keyword")!=null&&!product.get("keyword").equals("")){
                    Path<String> path=root.get("goodsIntro");
                    list.add(criteriaBuilder.like(path,"%"+product.get("keyword")+"%"));
                }
                if(product.get("goodsCategoryId")!=null){
                    Path<Long> path=root.get("goodsCategoryId");
                    list.add(criteriaBuilder.equal(path,product.get("goodsCategoryId")));
                }
                Predicate pp[]=new Predicate[list.size()];
                criteriaQuery.where(list.toArray(pp));
                return null;
            }
        });
    }

//    List<NewBeeMallGoods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);
//
//    int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

    //int batchInsert(@Param("newBeeMallGoodsList") List<NewBeeMallGoods> newBeeMallGoodsList);

    //int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

   // int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

}