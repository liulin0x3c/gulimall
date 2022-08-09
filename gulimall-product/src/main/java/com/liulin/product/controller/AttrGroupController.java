package com.liulin.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.liulin.product.entity.AttrAttrgroupRelationEntity;
import com.liulin.product.entity.AttrEntity;
import com.liulin.product.service.AttrAttrgroupRelationService;
import com.liulin.product.service.AttrService;
import com.liulin.product.service.CategoryService;
import com.liulin.product.vo.AttrAttrgroupRelationVo;
import com.liulin.product.vo.AttrGroupWithAttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.liulin.product.entity.AttrGroupEntity;
import com.liulin.product.service.AttrGroupService;
import com.liulin.common.utils.PageUtils;
import com.liulin.common.utils.R;



/**
 * 属性分组
 *
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:27
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationVo[] vos) {
         attrService.deleteRelation(vos);
        return R.ok();
    }



    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", entities);
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                            @RequestParam Map<String, Object> params
                            ) {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }

    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;

    @PostMapping("/attr/relation")
    public R saveAttrRelation(@RequestBody AttrAttrgroupRelationVo[] attrAttrgroupRelationVos) {
        for(AttrAttrgroupRelationVo attrAttrgroupRelationVo : attrAttrgroupRelationVos) {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(attrAttrgroupRelationVo, attrAttrgroupRelationEntity);
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }
        return R.ok();
    }

//    http://localhost:88/api/product/attrgroup/225/withattr
    @GetMapping("/{catelogId}/withattr")
    public R listAttrGroupWithAttr(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrVo> attrGroupWithAttrVos = attrGroupService.getattrGroupWithAttrVosByCateLogId(catelogId);
        return R.ok().put("data", attrGroupWithAttrVos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        System.out.println(catelogId);
        System.out.println((String) params.get("key"));
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
