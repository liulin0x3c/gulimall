package com.liulin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liulin.product.service.CategoryBrandRelationService;
import com.liulin.product.vo.LevelOneCategoryEntityDetailVo;
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
                new QueryWrapper<>()
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

    @Override
    public List<CategoryEntity> getCategoryEntityLevelOneList() {
        return this.list(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatLevel, 1));
    }

    @Override
    public LevelOneCategoryEntityDetailVo getLevelOneCategoryEntityDetailVo() {
        LevelOneCategoryEntityDetailVo levelOneCategoryEntityDetailVo = new LevelOneCategoryEntityDetailVo();
        List<CategoryEntity> categoryEntityLevelOneList = this.getCategoryEntityLevelOneList();
        //NULL
        categoryEntityLevelOneList.forEach(categoryEntity -> {
                Long catId = categoryEntity.getCatId();
                List<CategoryEntity> category2Entities = this.list(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, catId).eq(CategoryEntity::getCatLevel, 2));
                //NULL
            List<LevelOneCategoryEntityDetailVo.LeveTwoCategoryEntityDetailVo> leveTwoCategoryEntityDetailVos = category2Entities.stream().map(category2Entity -> {
                Long catId2 = category2Entity.getCatId();
                List<CategoryEntity> category3Entities = this.list(new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getParentCid, catId2).eq(CategoryEntity::getCatLevel, 3));
                //NULL
                List<LevelOneCategoryEntityDetailVo.LeveTwoCategoryEntityDetailVo.LevelTreeCategoryEntityDetailVo> levelTreeCategoryEntityDetailVoList = category3Entities.stream().map(category3Entity -> new LevelOneCategoryEntityDetailVo.LeveTwoCategoryEntityDetailVo.LevelTreeCategoryEntityDetailVo(catId2.toString(), category3Entity.getCatId().toString(), category3Entity.getName())).collect(Collectors.toList());
                return new LevelOneCategoryEntityDetailVo.LeveTwoCategoryEntityDetailVo(catId.toString(), category2Entity.getCatId().toString(), levelTreeCategoryEntityDetailVoList, category2Entity.getName());
            }).collect(Collectors.toList());
            levelOneCategoryEntityDetailVo.putChildCategoryEntity(catId, leveTwoCategoryEntityDetailVos);
        });
        return levelOneCategoryEntityDetailVo;
    }

    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        return all.stream().filter(
                categoryEntity -> Objects.equals(root.getCatId(), categoryEntity.getParentCid())
        ).peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, all))).sorted(
                Comparator.comparingInt(c -> (c.getSort() != null ? c.getSort() : 0))
        ).collect(Collectors.toList());
    }
}