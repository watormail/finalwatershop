package com.etc.service;


import com.etc.api.vo.NewBeeMallIndexCategoryVO;
import com.etc.entity.GoodsCategory;

import java.util.List;

public interface GoodsCategoryService {
    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 返回分类数据(首页调用)
     *
     * @return
     */
    List<NewBeeMallIndexCategoryVO> getCategoriesForIndex();
}
