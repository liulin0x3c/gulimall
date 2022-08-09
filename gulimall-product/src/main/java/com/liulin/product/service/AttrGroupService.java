package com.liulin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.product.entity.AttrGroupEntity;
import com.liulin.product.vo.AttrGroupWithAttrVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:27
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AttrGroupWithAttrVo> getattrGroupWithAttrVosByCateLogId(Long catelogId);
}

