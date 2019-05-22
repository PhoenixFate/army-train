/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.rzzk.entity.ArmyTree;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 军官Entity
 * @author shenming
 * @version 2019-03-22
 */
public class Officer extends DataEntity<Officer> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 姓名
	private Date birthday;		// 出生年月
	private String militaryRank;		// 军衔
	private ArmyTree unit;		// 单位
	
	public Officer() {
		super();
	}

	public Officer(String id){
		super(id);
	}

	@ExcelField(title="姓名", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="出生年月", align=2, sort=7)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	@ExcelField(title="军衔", align=2, sort=8)
	public String getMilitaryRank() {
		return militaryRank;
	}

	public void setMilitaryRank(String militaryRank) {
		this.militaryRank = militaryRank;
	}
	
	@ExcelField(title="单位", fieldType=ArmyTree.class, value="unit.name", align=2, sort=9)
	public ArmyTree getUnit() {
		return unit;
	}

	public void setUnit(ArmyTree unit) {
		this.unit = unit;
	}
	
}