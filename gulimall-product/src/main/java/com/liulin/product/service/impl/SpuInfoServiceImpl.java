package com.liulin.product.service.impl;

import com.liulin.product.dao.SpuInfoDescDao;
import com.liulin.product.entity.AttrEntity;
import com.liulin.product.entity.ProductAttrValueEntity;
import com.liulin.product.entity.SpuInfoDescEntity;
import com.liulin.product.service.*;
import com.liulin.product.vo.BaseAttrs;
import com.liulin.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.Query;

import com.liulin.product.dao.SpuInfoDao;
import com.liulin.product.entity.SpuInfoEntity;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        List<String> decript = spuSaveVo.getDecript();
        spuInfoDescEntity.setDecript(String.join(",", decript));
        Long spuInfoEntityId = spuInfoEntity.getId();
        spuInfoDescEntity.setSpuId(spuInfoEntityId);
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);

        List<String> imageUrls = spuSaveVo.getImages();
        spuImagesService.saveImages(spuInfoEntityId, imageUrls);

        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map(baseAttr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            Long attrId = baseAttr.getAttrId();
            productAttrValueEntity.setAttrId(attrId);
            AttrEntity attrEntity = attrService.getById(attrId);
            productAttrValueEntity.setAttrName(attrEntity.getAttrName());
            productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
            productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntityId);
            return productAttrValueEntity;
        }).collect(Collectors.toList());

        productAttrValueService.saveProductAttrValues(productAttrValueEntities);

    }



    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }


}