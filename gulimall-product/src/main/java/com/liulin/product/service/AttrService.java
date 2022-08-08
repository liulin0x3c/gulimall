package com.liulin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.product.entity.AttrEntity;
import com.liulin.product.vo.AttrAttrgroupRelationVo;
import com.liulin.product.vo.AttrRespVo;
import com.liulin.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:26
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void safeSave(AttrVo attr);

//    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId);

    AttrRespVo getAttrRespVoById(Long attrId);

    void safeUpdateById(AttrVo attrVo);

//    PageUtils querySaleAttrPage(Map<String, Object> params, Long catelogId);

    void safeRemoveByIds(List<Long> ids);

    PageUtils queryAttrPage(Map<String, Object> params, Long catelogId, String type);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrAttrgroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);


//    String getGroupName(AttrEntity attrEntity);
}

