package com.etc.dao;

import com.etc.entity.Carousel;
import com.etc.entity.GoodsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface GoodsCategoryDao extends JpaSpecificationExecutor<GoodsCategory>, JpaRepository<GoodsCategory,Long>, Serializable {
    public GoodsCategory findByCategoryId(long categoryId);
    public GoodsCategory findByCategoryLevelAndCategoryName(Byte categoryLevel,String categoryName);
    //获取带分页pageable的分类信息
    public  List<GoodsCategory> findByParentIdInAndAndCategoryLevel(List<Long> parentIds,byte categoryLevel, Pageable pageable);
    //获取不带分页限制的分类信息
    public  List<GoodsCategory> findByParentIdInAndAndCategoryLevel(List<Long> parentIds,byte categoryLevel);
    public List<GoodsCategory> findByCategoryIdIn(long[] ids);
}
