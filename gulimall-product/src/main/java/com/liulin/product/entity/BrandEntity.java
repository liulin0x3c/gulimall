package com.liulin.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotEmpty(message = "品牌名称必须填写")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "logo不能为空")
	@URL(message = "logo地址必须为有效的url")
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母只包含一个字母")
	private String firstLetter;
	/**
	 * 排序
	 */
	@PositiveOrZero(message = "排序权重必须是正数")
	private Integer sort;

}
