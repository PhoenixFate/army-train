/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;


import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 体能训练项Entity
 * @author shenming
 * @version 2019-03-25
 */
public class TrainPhysical extends DataEntity<TrainPhysical> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 体能训练项
	
	public TrainPhysical() {
		super();
	}

	public TrainPhysical(String id){
		super(id);
	}

	@ExcelField(title="体能训练项", align=2, sort=6)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}