/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.dao;

import com.jeeplus.modules.rzzk.entity.ArmyTree;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.rzzk.entity.Officer;

/**
 * 军官DAO接口
 * @author shenming
 * @version 2019-03-22
 */
@MyBatisDao
public interface OfficerDao extends CrudDao<Officer> {

	public List<ArmyTree> findListByunit(ArmyTree unit);
	
}