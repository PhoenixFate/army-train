/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.jeeplus.common.persistence.TreeEntity;

/**
 * 树测试Entity
 * @author shenming
 * @version 2019-03-19
 */
public class MyTree extends TreeEntity<MyTree> {
	
	private static final long serialVersionUID = 1L;
	private MyTree parent;		// 父级编号
	private String parentIds;		// 所有父级编号
	private String name;		// 名称
	private Integer sort;		// 排序
	private String code;		// 编号
	
	public MyTree() {
		super();
	}

	public MyTree(String id){
		super(id);
	}

	@JsonBackReference
	public MyTree getParent() {
		return parent;
	}

	public void setParent(MyTree parent) {
		this.parent = parent;
	}
	
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}