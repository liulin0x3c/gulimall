package com.liulin.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liulin.common.utils.PageUtils;
import com.liulin.product.entity.CommentReplayEntity;

import java.util.Map;

/**
 * 商品评价回复关系
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:26
 */
public interface CommentReplayService extends IService<CommentReplayEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

