package com.liulin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:26
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveFilledBrandNameAndCategoryName(CategoryBrandRelationEntity categoryBrandRelation);

    void updateBrand(Long brandId, String brandName);

    void updateCategory(Long catId, String name);

    List<CategoryBrandRelationEntity> getBrandsByCatId(Long catId);
}

