/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.rzzk.entity.ArmyTree;
import javax.validation.constraints.NotNull;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 学员Entity
 * @author shenming
 * @version 2019-03-25
 */
public class Trainee extends DataEntity<Trainee> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private String sex;		// 性别
	private Date birthday;		// 出生年月
	private ArmyTree unit;		// 所属单位
	
	public Trainee() {
		super();
	}

	public Trainee(String id){
		super(id);
	}

	@ExcelField(title="姓名", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="性别", dictType="sex", align=2, sort=7)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="出生年月", align=2, sort=8)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@NotNull(message="所属单位不能为空")
	@ExcelField(title="所属单位", fieldType=ArmyTree.class, value="unit.name", align=2, sort=9)
	public ArmyTree getUnit() {
		return unit;
	}

	public void setUnit(ArmyTree unit) {
		this.unit = unit;
	}
	
}