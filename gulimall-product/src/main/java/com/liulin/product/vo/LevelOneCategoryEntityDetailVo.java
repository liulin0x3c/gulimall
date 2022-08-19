package com.liulin.product.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

public class LevelOneCategoryEntityDetailVo extends HashMap<String, List<LevelOneCategoryEntityDetailVo.LeveTwoCategoryEntityDetailVo>> {
    public void putChildCategoryEntity(Long categoryId, List<LeveTwoCategoryEntityDetailVo> leveTwoCategoryEntityDetailVoList) {
        this.put(categoryId.toString(),leveTwoCategoryEntityDetailVoList);
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeveTwoCategoryEntityDetailVo {

        /**
         * 一级父分类的id
         */
        private String catalog1Id;

        private String id;
        /**
         * 三级子分类
         */
        private List<LevelTreeCategoryEntityDetailVo> catalog3List;

        private String name;
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class LevelTreeCategoryEntityDetailVo {

            /**
             * 父分类、二级分类id
             */
            private String catalog2Id;

            private String id;

            private String name;
        }

    }

}

