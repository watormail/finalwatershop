package com.etc.Service;

import com.etc.dao.GoodsCategoryDao;
import com.etc.entity.GoodsCategory;
import com.etc.service.GoodsCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class GoodsCategoryServiceTest {
    @Autowired
    GoodsCategoryService goodsCategoryService;
    @Test
    public void findBylevelAndParentIdAndNum(){
        System.out.println(goodsCategoryService.getCategoriesForIndex());
    }
}
