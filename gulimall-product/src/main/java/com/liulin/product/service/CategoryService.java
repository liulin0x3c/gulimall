package com.liulin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.product.entity.CategoryEntity;
import com.liulin.product.vo.LevelOneCategoryEntityDetailVo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:26
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenusByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    void safeUpdateById(CategoryEntity category);

    List<CategoryEntity> getCategoryEntityLevelOneList();

    LevelOneCategoryEntityDetailVo getLevelOneCategoryEntityDetailVo();
}

