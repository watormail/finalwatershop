package com.etc.dao;

import com.etc.entity.Carousel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CarouselDaoTest {
    @Autowired
    CarouselDao carouselDao;
    @Test
    public void findByCarouselDao(){
        Carousel carousel=carouselDao.findByCarouselId(1);
        System.out.println(carousel);
    }
}
