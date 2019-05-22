/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.dao;

import com.jeeplus.common.persistence.TreeDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.rzzk.entity.MyTree;

/**
 * 树测试DAO接口
 * @author shenming
 * @version 2019-03-19
 */
@MyBatisDao
public interface MyTreeDao extends TreeDao<MyTree> {
	
}