package com.liulin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.product.entity.PmsCategoryBrandRelationEntity;

import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2022-07-20 23:24:37
 */
public interface PmsCategoryBrandRelationService extends IService<PmsCategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

