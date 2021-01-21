package com.etc.service;

import com.etc.api.vo.NewBeeMallIndexCarouselVO;

import java.util.List;

public interface CarouselService {
    /**
     * 返回固定数量的轮播图对象(首页调用)
     *
     * @param number
     * @return
     */
    List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number);
}
