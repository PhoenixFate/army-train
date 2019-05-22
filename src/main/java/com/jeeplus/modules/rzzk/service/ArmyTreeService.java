/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.service.TreeService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.rzzk.entity.ArmyTree;
import com.jeeplus.modules.rzzk.dao.ArmyTreeDao;

/**
 * 部队组织结构Service
 * @author shengming
 * @version 2019-03-22
 */
@Service
@Transactional(readOnly = true)
public class ArmyTreeService extends TreeService<ArmyTreeDao, ArmyTree> {

	public ArmyTree get(String id) {
		return super.get(id);
	}
	
	public List<ArmyTree> findList(ArmyTree armyTree) {
		if (StringUtils.isNotBlank(armyTree.getParentIds())){
			armyTree.setParentIds(","+armyTree.getParentIds()+",");
		}
		return super.findList(armyTree);
	}
	
	@Transactional(readOnly = false)
	public void save(ArmyTree armyTree) {
		super.save(armyTree);
	}
	
	@Transactional(readOnly = false)
	public void delete(ArmyTree armyTree) {
		super.delete(armyTree);
	}
	
}