package com.liulin.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liulin.product.dao.AttrAttrgroupRelationDao;
import com.liulin.product.dao.AttrDao;
import com.liulin.product.entity.AttrAttrgroupRelationEntity;
import com.liulin.product.entity.AttrEntity;
import com.liulin.product.service.AttrAttrgroupRelationService;
import com.liulin.product.vo.AttrGroupWithAttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;

import com.liulin.product.dao.AttrGroupDao;
import com.liulin.product.entity.AttrGroupEntity;
import com.liulin.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
//        if(catelogId == 0) {
//            return queryPage(params);
//        }
        LambdaQueryWrapper<AttrGroupEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if(catelogId != 0) {
            wrapper.eq(AttrGroupEntity::getCatelogId, catelogId);
        }
        if(!StringUtils.isEmpty(key)) {
            wrapper.and((obj)-> obj.eq(AttrGroupEntity::getAttrGroupId, key).or().like(AttrGroupEntity::getAttrGroupName, key));
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Autowired
    AttrDao attrDao;

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Override
    public List<AttrGroupWithAttrVo> getattrGroupWithAttrVosByCateLogId(Long catelogId) {
        List<AttrGroupEntity> attrGroupEntities = this.list(new LambdaQueryWrapper<AttrGroupEntity>().eq(AttrGroupEntity::getCatelogId, catelogId));
        List<AttrGroupWithAttrVo> attrAttrGroupRelationVos = attrGroupEntities.stream().map(attrGroupEntity -> {
            AttrGroupWithAttrVo attrAttrGroupRelationVo = new AttrGroupWithAttrVo();
            BeanUtils.copyProperties(attrGroupEntity, attrAttrGroupRelationVo);
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>().eq(AttrAttrgroupRelationEntity::getAttrGroupId, attrAttrGroupRelationVo.getAttrGroupId()));
            List<Long> attrIds = null;
            if (!attrAttrgroupRelationEntities.isEmpty()) {
                attrIds = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
            }
            List<AttrEntity> attrEntities = attrDao.selectBatchIds(attrIds);
            attrAttrGroupRelationVo.setAttrs(attrEntities);
            return attrAttrGroupRelationVo;
        }).collect(Collectors.toList());
        return attrAttrGroupRelationVos;
    }

}