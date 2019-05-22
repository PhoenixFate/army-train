/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 学期Entity
 * @author shenming
 * @version 2019-04-10
 */
public class ArmyItem extends DataEntity<ArmyItem> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 学期名
	
	public ArmyItem() {
		super();
	}

	public ArmyItem(String id){
		super(id);
	}

	@ExcelField(title="学期名", align=2, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}