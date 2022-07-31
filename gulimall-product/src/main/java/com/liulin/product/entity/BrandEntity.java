package com.liulin.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.liulin.valid.ListValues;
import com.liulin.valid.SaveGroup;
import com.liulin.valid.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author liulin0x3c
 * @email liulin0x3c@gmail.com
 * @date 2022-07-21 01:26:26
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
//	@NotNull(groups = {UpdateGroup.class}, message = "修改时,必须指定品牌id")
//	@Null(groups = {SaveGroup.class}, message = "新增时,品牌id不能指定")
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
//	@NotBlank(groups = {SaveGroup.class}, message = "新增时候品牌名称必须填写")
	private String name;
	/**
	 * 品牌logo地址
	 */
//	@NotEmpty(message = "logo不能为空")
//	@URL(message = "logo地址必须为有效的url")
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValues(values = {0,1})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
//	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母只包含一个字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@PositiveOrZero(message = "排序权重必须是正数")
	private Integer sort;

}
