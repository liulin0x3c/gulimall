package com.liulin.product.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liulin.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;

import com.liulin.product.dao.CategoryDao;
import com.liulin.product.entity.CategoryEntity;
import com.liulin.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        return entities.stream().filter(
                categoryEntity -> Objects.equals(categoryEntity.getParentCid(), 0L)
        ).peek(menu-> menu.setChildren(getChildren(menu, entities))).sorted(
                Comparator.comparingInt(c -> (c.getSort() != null ? c.getSort() : 0))
        ).collect(Collectors.toList());
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 检查当前要删除的2菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        CategoryEntity categoryEntity = getById(catelogId);
        Long parentCid = categoryEntity.getParentCid();
        while (!Objects.equals(parentCid, 0L)) {
            path.add(catelogId);
            catelogId = parentCid;
            categoryEntity = getById(catelogId);
            parentCid = categoryEntity.getParentCid();
        }
        path.add(catelogId);
        Collections.reverse(path);
        return path.toArray(new Long[0]);
    }


    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public void safeUpdateById(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream().filter(
                categoryEntity -> Objects.equals(root.getCatId(), categoryEntity.getParentCid())
        ).peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, all))).sorted(
                Comparator.comparingInt(c -> (c.getSort() != null ? c.getSort() : 0))
        ).collect(Collectors.toList());
    }
}