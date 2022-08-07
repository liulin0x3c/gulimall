package com.liulin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import com.liulin.product.vo.AttrRespVo;
import com.liulin.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
        Long attrId = attrEntity.getAttrId();
        Long attrGroupId = attr.getAttrGroupId();
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
        attrAttrgroupRelationEntity.setAttrId(attrId);
        attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
    }

    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        LambdaQueryWrapper<AttrEntity> queryWrapper = new LambdaQueryWrapper<>();
        if(catelogId != 0L) {
            queryWrapper.eq(AttrEntity::getCatelogId, catelogId);
        }
        String key = (String) params.get("key");

        if(StringUtils.isNotEmpty(key)) {
            queryWrapper.and((wrapper) ->
                    wrapper.eq(AttrEntity::getAttrId,key).or().like(AttrEntity::getAttrName, key));
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        List<AttrEntity> attrs = page.getRecords();
        List<AttrRespVo> respVos = attrs.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            String groupName = getGroupName(attrEntity);
            attrRespVo.setGroupName(groupName);

            String catelogName = getCatelogName(attrEntity);
            attrRespVo.setCatelogName(catelogName);

            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(respVos);
        return pageUtils;
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

    //    @Override
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