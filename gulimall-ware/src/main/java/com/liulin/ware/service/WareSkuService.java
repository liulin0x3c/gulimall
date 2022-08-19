package com.liulin.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.to.SkuHasStockTo;
import com.liulin.common.utils.PageUtils;
import com.liulin.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 02:06:20
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuHasStockTo> getStockBatchByIds(List<Long> ids);
}

