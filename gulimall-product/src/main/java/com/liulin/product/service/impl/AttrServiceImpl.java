package com.liulin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import com.liulin.product.vo.AttrRespVo;
import com.liulin.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveAttr(AttrVo attr) {
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
//            groupName
            Long attrId = attrRespVo.getAttrId();
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrId, attrId));
            String groupName = null;
            if (attrAttrgroupRelationEntity != null) {
                Long attrGroupId = attrAttrgroupRelationEntity.getAttrGroupId();
                LambdaQueryWrapper<AttrGroupEntity> queryWrapper1 =
                        new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getAttrGroupId, attrGroupId);
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(queryWrapper1);
                if (attrGroupEntity != null) {
                    groupName = attrGroupEntity.getAttrGroupName();
                    System.out.println("groupName:" + groupName);
                }
            }

//            catelogname
            LambdaQueryWrapper<CategoryEntity> queryWrapper2 =
                    new LambdaQueryWrapper<CategoryEntity>().eq(CategoryEntity::getCatId, attrRespVo.getCatelogId());
            CategoryEntity categoryEntity = categoryDao.selectOne(queryWrapper2);
            String catelogname = null;
            if (categoryEntity != null) {
                catelogname = categoryEntity.getName();
                System.out.println("catelogname:" + catelogname);
            }

            attrRespVo.setGroupName(groupName);
            attrRespVo.setCatelogName(catelogname);

            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(respVos);
        return pageUtils;
    }
}