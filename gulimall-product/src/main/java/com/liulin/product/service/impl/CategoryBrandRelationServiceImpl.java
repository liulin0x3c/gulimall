package com.liulin.product.service.impl;

import com.liulin.product.dao.BrandDao;
import com.liulin.product.dao.CategoryDao;
import com.liulin.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;

import com.liulin.product.dao.CategoryBrandRelationDao;
import com.liulin.product.entity.CategoryBrandRelationEntity;
import com.liulin.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void savefilledBrandNameAndCategoryName(CategoryBrandRelationEntity categoryBrandRelation) {
        String categoryName = categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName();
        String brandname = brandDao.selectById(categoryBrandRelation.getBrandId()).getName();
        categoryBrandRelation.setBrandName(brandname);
        categoryBrandRelation.setCatelogName(categoryName);
        this.save(categoryBrandRelation);
    }

}