package com.liulin.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.liulin.product.entity.ProductAttrValueEntity;
import com.liulin.product.service.ProductAttrValueService;
import com.liulin.product.vo.AttrRespVo;
import com.liulin.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liulin.product.service.AttrService;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.R;



/**
 * 商品属性
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:26
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);
        return R.ok().put("page", page);
    }


    @Autowired
    private ProductAttrValueService productAttrValueService;
    @GetMapping("/base/listforspu/{spuId}")
    //http://localhost:88/api/product/attr/base/listforspu/11
    public R baseAttrListForSpu(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> entityList = productAttrValueService.baseAttrListForSpu(spuId);
        return R.ok().put("data",entityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
//        AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attr = attrService.getAttrRespVoById(attrId);
        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.safeSave(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attrVo){
        attrService.safeUpdateById(attrVo);

        return R.ok();
    }
    @PostMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R updateSpuId(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> productAttrValueEntityList){
        productAttrValueService.updateSpuAttr(spuId, productAttrValueEntityList);
//        productAttrValueService.updateBatchById(productAttrValueEntityList);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.safeRemoveByIds(Arrays.asList(attrIds));
        return R.ok();
    }

//    http://localhost:88/api/product/attr/base/list/0?t=1659803282778&page=1&limit=10&key=
//    @RequestMapping("/base/list/{catelogId}")
//    //@RequiresPermissions("product:attr:list")
//    public R baseList(@RequestParam Map<String, Object> params, @PathVariable Long catelogId){
//        PageUtils page = attrService.queryBaseAttrPage(params, catelogId);
//        return R.ok().put("page", page);
//    }


    @RequestMapping("/{type}/list/{catelogId}")
    //@RequiresPermissions("product:attr:list")
    public R typeList(@RequestParam Map<String, Object> params, @PathVariable Long catelogId,
                      @PathVariable String type) {
        PageUtils page = attrService.queryAttrPage(params, catelogId, type);
        return R.ok().put("page", page);
    }
}
