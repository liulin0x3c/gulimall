package com.liulin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.constant.ProductConstant;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;
import com.liulin.product.dao.AttrAttrgroupRelationDao;
import com.liulin.product.dao.AttrDao;
import com.liulin.product.dao.AttrGroupDao;
import com.liulin.product.dao.CategoryDao;
import com.liulin.product.entity.AttrAttrgroupRelationEntity;
import com.liulin.product.entity.AttrEntity;
import com.liulin.product.entity.AttrGroupEntity;
import com.liulin.product.entity.CategoryEntity;
import com.liulin.product.service.AttrService;
import com.liulin.product.service.CategoryService;
import com.liulin.product.vo.AttrAttrgroupRelationVo;
import com.liulin.product.vo.AttrRespVo;
import com.liulin.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Transactional
    @Override
    public void safeSave(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.save(attrEntity);
        if(Objects.equals(attrEntity.getAttrType(), ProductConstant.AttrType.BASE.getCode())) {
            Long attrId = attrEntity.getAttrId();
            Long attrGroupId = attr.getAttrGroupId();
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            attrAttrgroupRelationEntity.setAttrId(attrId);
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;


    @Override
    public PageUtils queryAttrPage(Map<String, Object> params, Long catelogId, String type) {
        LambdaQueryWrapper<AttrEntity> queryWrapper =
                new LambdaQueryWrapper<>();
        if("sale".equalsIgnoreCase(type)) {
            queryWrapper.eq(AttrEntity::getAttrType, ProductConstant.AttrType.SALE.getCode());
        }
        if(catelogId != 0L) {
            queryWrapper.eq(AttrEntity::getCatelogId, catelogId);
        }
        IPage<AttrEntity> page = fuzzyPageQuery(params, queryWrapper);
        List<AttrEntity> attrs = page.getRecords();
        List<AttrRespVo> respVos = attrs.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            if("sale".equalsIgnoreCase(type)) {
                String groupName = getGroupName(attrEntity);
                attrRespVo.setGroupName(groupName);
            }

            String catelogName = getCatelogName(attrEntity);
            attrRespVo.setCatelogName(catelogName);

            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrgroupId);
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(queryWrapper);
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        List<AttrEntity> entities = null;
        if (!attrIds.isEmpty()) {
            entities = this.listByIds(attrIds);
        }
        return entities;
    }

    /**
     * 获取当前分组没有关联的所有属性
     */
    @Override
    public PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
//        当前分组所在分类的id
        Long catelogId = attrGroupEntity.getCatelogId();
        LambdaQueryWrapper<AttrGroupEntity> queryWrapper =
                new LambdaQueryWrapper<AttrGroupEntity>()
                        .eq(AttrGroupEntity::getCatelogId, catelogId);
//        当前分组所在分类下的所有分组attrGroupEntities
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(queryWrapper);
//        当前分组所在分类下的所有分组id
        List<Long> attrGroupIds = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                .in(AttrAttrgroupRelationEntity::getAttrGroupId, attrGroupIds);
        //        这些分组关联的属性(需要被排除掉)
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(wrapper);
        //        这些分组关联的属性的id(需要被排除掉)
        List<Long> groupedAttrIds = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
//        排除他们，从attr表中找到需要的attrEntity
        LambdaQueryWrapper<AttrEntity> queryWrapper1 = new LambdaQueryWrapper<AttrEntity>().eq(AttrEntity::getCatelogId, catelogId)
                .eq(AttrEntity::getAttrType, ProductConstant.AttrType.BASE.getCode());
        if(!groupedAttrIds.isEmpty()) {
            queryWrapper1.notIn(AttrEntity::getAttrId, groupedAttrIds);
        }
        return new PageUtils(fuzzyPageQuery(params, queryWrapper1));
    }
    private IPage<AttrEntity> fuzzyPageQuery(Map<String, Object> params, LambdaQueryWrapper<AttrEntity> queryWrapper) {
        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper1) ->
                    wrapper1.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName, key));
        }
        return this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
    }


    @Override
    public void deleteRelation(AttrAttrgroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> entityList = Arrays.stream(vos).map((vo) -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(vo, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationDao.deleteBatchRelation(entityList);

    }

    @Override
    public AttrRespVo getAttrRespVoById(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        // groupId
        Long attrGroupId = getGroupId(attrEntity);
        attrRespVo.setAttrGroupId(attrGroupId);
        //groupName
        String groupName = getGroupName(attrGroupId);
        attrRespVo.setGroupName(groupName);
        //catelogName
        String catelogName = getCatelogName(attrEntity);
        attrRespVo.setCatelogName(catelogName);
        //catelogPath
        Long[] catelogPath = getCatelogPath(attrEntity);
        attrRespVo.setCatelogPath(catelogPath);

        return attrRespVo;
    }

    @Override
    public void safeUpdateById(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        this.updateById(attrEntity);
        if (Objects.equals(attrVo.getAttrType(), ProductConstant.AttrType.BASE.getCode())) {
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new LambdaQueryWrapper<>();
            Long attrId = attrVo.getAttrId();
            queryWrapper.eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
            Long count = attrAttrgroupRelationDao.selectCount(queryWrapper);

            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrId);

            Long attrGroupId = attrVo.getAttrGroupId();
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);

            if(count != 0) {
                LambdaUpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
                attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, updateWrapper);
            }else {
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
            }
        }

    }

    @Override
    public void safeRemoveByIds(List<Long> ids) {
        this.removeByIds(ids);
        for (Long attrId : ids) {
            LambdaUpdateWrapper<AttrAttrgroupRelationEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(AttrAttrgroupRelationEntity::getAttrId, attrId);
            attrAttrgroupRelationDao.delete(updateWrapper);
        }
    }



    private String getGroupName(AttrEntity attrEntity) {
        Long attrGroupId = getGroupId(attrEntity);
        return getGroupName(attrGroupId);
    }
    private String getGroupName(Long attrGroupId) {
        if(attrGroupId == null) {
            return null;
        }
        LambdaQueryWrapper<AttrGroupEntity> queryWrapper1 =
                new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getAttrGroupId, attrGroupId);
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(queryWrapper1);
        String groupName = null;
        if (attrGroupEntity != null) {
            groupName = attrGroupEntity.getAttrGroupName();
        }
        return groupName;
    }
    private Long getGroupId(AttrEntity attrEntity) {
        Long attrId = attrEntity.getAttrId();
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity =
                attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
        Long attrGroupId = null;
        if (attrAttrgroupRelationEntity != null) {
            attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
        }
        return attrGroupId;
    }

    private String getCatelogName(AttrEntity attrEntity) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper =
                new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, attrEntity.getCatelogId());
        CategoryEntity categoryEntity = categoryDao.selectOne(queryWrapper);
        String catelogName = null;
        if (categoryEntity != null) {
            catelogName = categoryEntity.getName();
        }
        return catelogName;
    }

    @Autowired
    CategoryService categoryService;
    private Long[] getCatelogPath(AttrEntity attrEntity) {
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = null;
        if (catelogId != null) {
            catelogPath = categoryService.findCatelogPath(catelogId);
        }
        return catelogPath;
    }
}