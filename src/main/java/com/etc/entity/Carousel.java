package com.etc.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
@Data
@Entity
@Table(name = "tb_newbee_mall_carousel")
public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carouselId;

    private String carouselUrl;

    private String redirectUrl;

    private Integer carouselRank;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Integer createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer updateUser;
}
