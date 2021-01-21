package com.etc.dao;

import com.etc.entity.Carousel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

public interface CarouselDao extends JpaSpecificationExecutor<Carousel>, JpaRepository<Carousel,Integer>, Serializable {
    public Carousel findByCarouselId(int id);
    @Query(value = "  select * from tb_newbee_mall_carousel" +
            "        where is_deleted = 0" +
            "        order by carousel_rank desc" +
            "        limit ?1",nativeQuery = true)
    public List<Carousel> findCarouselsByNum(int num);
}
