/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.rzzk.entity.MyTree;
import com.jeeplus.modules.rzzk.dao.MyTreeDao;

/**
 * 树测试Service
 * @author shenming
 * @version 2019-03-19
 */
@Service
@Transactional(readOnly = true)
public class MyTreeService extends TreeService<MyTreeDao, MyTree> {

	public MyTree get(String id) {
		return super.get(id);
	}
	
	public List<MyTree> findList(MyTree myTree) {
		if (StringUtils.isNotBlank(myTree.getParentIds())){
			myTree.setParentIds(","+myTree.getParentIds()+",");
		}
		return super.findList(myTree);
	}
	
	@Transactional(readOnly = false)
	public void save(MyTree myTree) {
		super.save(myTree);
	}
	
	@Transactional(readOnly = false)
	public void delete(MyTree myTree) {
		super.delete(myTree);
	}
	
}