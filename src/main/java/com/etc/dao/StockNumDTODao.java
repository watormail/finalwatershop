package com.etc.dao;

import com.etc.entity.NewBeeMallShoppingCartItem;
import com.etc.entity.StockNumDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

public interface StockNumDTODao extends JpaSpecificationExecutor<StockNumDTO>, JpaRepository<StockNumDTO,Long>, Serializable {
}
