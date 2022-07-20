package com.liulin.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.member.entity.GrowthChangeHistoryEntity;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:34:09
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

