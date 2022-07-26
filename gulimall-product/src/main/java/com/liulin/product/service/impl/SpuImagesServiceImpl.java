package com.liulin.product.service.impl;

import com.liulin.product.entity.SkuInfoEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;

import com.liulin.product.dao.SpuImagesDao;
import com.liulin.product.entity.SpuImagesEntity;
import com.liulin.product.service.SpuImagesService;
import org.springframework.util.CollectionUtils;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveImages(Long spuInfoEntityId, List<String> imageUrls) {
        if(CollectionUtils.isEmpty(imageUrls)) return;

        List<SpuImagesEntity> spuImagesEntities = imageUrls.stream().map(url -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntityId);
            spuImagesEntity.setImgUrl(url);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        this.saveBatch(spuImagesEntities);

    }




}