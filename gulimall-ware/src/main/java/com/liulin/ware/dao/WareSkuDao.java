package com.liulin.ware.dao;

import com.liulin.common.to.SkuHasStockTo;
import com.liulin.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 02:06:20
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Long getStockBySkuId(@Param("id") Long id);
}
