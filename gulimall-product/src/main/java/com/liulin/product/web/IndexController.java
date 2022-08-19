package com.liulin.product.web;


import com.liulin.product.entity.CategoryEntity;
import com.liulin.product.service.CategoryService;
import com.liulin.product.vo.LevelOneCategoryEntityDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {


    @Autowired
    CategoryService categoryService;

    @GetMapping({"/","/index/html"})
    public String indexPage(Model model) {
        List<CategoryEntity> categoryEntityLevelOneList = categoryService.getCategoryEntityLevelOneList();
        model.addAttribute("categorys",categoryEntityLevelOneList);
        return "index";
    }

    @GetMapping("index/catalog.json")
    @ResponseBody
    public LevelOneCategoryEntityDetailVo indexCateLogJson() {
        return categoryService.getLevelOneCategoryEntityDetailVo();
    }


}
