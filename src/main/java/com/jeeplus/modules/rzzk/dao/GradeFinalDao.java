/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.dao;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.ArmyItem;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.rzzk.entity.GradeFinal;

/**
 * 考核成绩DAO接口
 * @author shengming
 * @version 2019-04-10
 */
@MyBatisDao
public interface GradeFinalDao extends CrudDao<GradeFinal> {

	public List<Trainee> findListBytrainee(Trainee trainee);
	public List<TrainItem> findListBytrainItem(TrainItem trainItem);
	public List<ArmyItem> findListByarmyItem(ArmyItem armyItem);
	
}