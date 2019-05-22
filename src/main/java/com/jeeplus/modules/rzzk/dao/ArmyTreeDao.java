/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.dao;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.rzzk.entity.ArmyTree;

/**
 * 部队组织结构DAO接口
 * @author shengming
 * @version 2019-03-22
 */
@MyBatisDao
public interface ArmyTreeDao extends TreeDao<ArmyTree> {
	
}