package com.liulin.product.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.liulin.product.dao.AttrAttrgroupRelationDao;
import com.liulin.product.entity.AttrAttrgroupRelationEntity;
import com.liulin.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;

import com.liulin.product.dao.AttrDao;
import com.liulin.product.entity.AttrEntity;
import com.liulin.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;

import javax.management.relation.RelationService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
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
}