package com.etc.service.impl;

import com.etc.api.vo.NewBeeMallIndexCategoryVO;
import com.etc.api.vo.SecondLevelCategoryVO;
import com.etc.api.vo.ThirdLevelCategoryVO;
import com.etc.common.NewBeeMallCategoryLevelEnum;
import com.etc.common.ServiceResultEnum;
import com.etc.dao.GoodsCategoryDao;
import com.etc.entity.GoodsCategory;
import com.etc.service.GoodsCategoryService;
import com.etc.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.print.Pageable;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class GoodsCategoryServiceimpl implements GoodsCategoryService {
    @Autowired
    GoodsCategoryDao goodsCategoryDao;
    public boolean aBooleanSaveMethod(GoodsCategory goodsCategory){
        if(goodsCategory!=null) {
            GoodsCategory goodsCategory1=goodsCategoryDao.save(goodsCategory);
            if(goodsCategory1!=null){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    @Override
    public String saveCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = goodsCategoryDao.findByCategoryLevelAndCategoryName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp != null) {
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        if (aBooleanSaveMethod(goodsCategory)) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateGoodsCategory(GoodsCategory goodsCategory) {
        GoodsCategory temp = goodsCategoryDao.findByCategoryId(goodsCategory.getCategoryId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        GoodsCategory temp2 = goodsCategoryDao.findByCategoryLevelAndCategoryName(goodsCategory.getCategoryLevel(), goodsCategory.getCategoryName());
        if (temp2 != null && !temp2.getCategoryId().equals(goodsCategory.getCategoryId())) {
            //同名且不同id 不能继续修改
            return ServiceResultEnum.SAME_CATEGORY_EXIST.getResult();
        }
        goodsCategory.setUpdateTime(new Date());
        if (aBooleanSaveMethod(goodsCategory)) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public GoodsCategory getGoodsCategoryById(Long id) {
        return goodsCategoryDao.findByCategoryId(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        int mark=0;
        for(int i=0;i<ids.length;i++){
            long id1=(long)ids[i];
            GoodsCategory goodsCategory=goodsCategoryDao.findByCategoryId(id1);
            if(goodsCategory!=null) {
                goodsCategory.setIsDeleted((byte) 1);
                goodsCategoryDao.save(goodsCategory);
                mark++;
            }
        }
        if(mark>0) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<NewBeeMallIndexCategoryVO> getCategoriesForIndex() {
        List<NewBeeMallIndexCategoryVO> newBeeMallIndexCategoryVOS = new ArrayList<>();//初始化一级分类vo,我也不知道这vo是什么鬼
        PageRequest pr = PageRequest.of(0,10);
        //获取一级分类的固定数量的数据
        List<GoodsCategory> firstLevelCategories = goodsCategoryDao.findByParentIdInAndAndCategoryLevel(Collections.singletonList(0L), (byte)NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel(), pr);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
            //获取二级分类的数据
            List<GoodsCategory> secondLevelCategories = goodsCategoryDao.findByParentIdInAndAndCategoryLevel(firstLevelCategoryIds, (byte)NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                List<Long> secondLevelCategoryIds = secondLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
                //获取三级分类的数据
                List<GoodsCategory> thirdLevelCategories = goodsCategoryDao.findByParentIdInAndAndCategoryLevel(secondLevelCategoryIds, (byte)NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    //根据 parentId 将 thirdLevelCategories 分组
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();
                    //处理二级分类
                    for (GoodsCategory secondLevelCategory : secondLevelCategories) {
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //如果该二级分类下有数据则放入 secondLevelCategoryVOS 对象中
                        if (thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())) {
                            //根据二级分类的id取出thirdLevelCategoryMap分组中的三级分类list
                            List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS((BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class)));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }
                    //处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)) {
                        //根据 parentId 将 thirdLevelCategories 分组
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for (GoodsCategory firstCategory : firstLevelCategories) {
                            NewBeeMallIndexCategoryVO newBeeMallIndexCategoryVO = new NewBeeMallIndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, newBeeMallIndexCategoryVO);
                            //如果该一级分类下有数据则放入 newBeeMallIndexCategoryVOS 对象中
                            if (secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())) {
                                //根据一级分类的id取出secondLevelCategoryVOMap分组中的二级级分类list
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                newBeeMallIndexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                newBeeMallIndexCategoryVOS.add(newBeeMallIndexCategoryVO);
                            }
                        }
                    }
                }
            }
            return newBeeMallIndexCategoryVOS;
        } else {
            return null;
        }
    }
}
