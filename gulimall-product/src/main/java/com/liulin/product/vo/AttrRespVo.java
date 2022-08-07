package com.liulin.product.vo;

import com.liulin.product.entity.AttrEntity;
import lombok.Data;

@Data
public class AttrRespVo extends AttrVo {
    private String catelogName;
    private String groupName;

    private Long[] catelogPath;

}
