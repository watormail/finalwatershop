package com.etc.Service;

import com.etc.api.vo.NewBeeMallIndexCarouselVO;
import com.etc.service.CarouselService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CarouselServiceTest {
    @Autowired
    CarouselService carouselService;
    @Test
    public void findBylevelAndParentIdAndNum(){
        List<NewBeeMallIndexCarouselVO> list=carouselService.getCarouselsForIndex(5);
        for (NewBeeMallIndexCarouselVO n:list
        ) {
            System.out.println(n);
        }
    }
    @Test
    public void outinttobyte(){
        int a=10;
        System.out.println((byte)a);
    }
}
