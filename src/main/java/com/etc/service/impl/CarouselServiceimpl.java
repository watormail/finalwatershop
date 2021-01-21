package com.etc.service.impl;

import com.etc.api.vo.NewBeeMallIndexCarouselVO;
import com.etc.dao.CarouselDao;
import com.etc.entity.Carousel;
import com.etc.service.CarouselService;
import com.etc.util.BeanUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarouselServiceimpl implements CarouselService {
    @Autowired
    CarouselDao carouselDao;
    @Override
    public List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<NewBeeMallIndexCarouselVO> newBeeMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselDao.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            newBeeMallIndexCarouselVOS = BeanUtil.copyList(carousels, NewBeeMallIndexCarouselVO.class);
        }
        return newBeeMallIndexCarouselVOS;
    }
}
