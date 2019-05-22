/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.dao;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.rzzk.entity.GradeDaily;

/**
 * 日常训练成绩DAO接口
 * @author shenming
 * @version 2019-03-25
 */
@MyBatisDao
public interface GradeDailyDao extends CrudDao<GradeDaily> {

	public List<Trainee> findListBytrainee(Trainee trainee);
	public List<TrainItem> findListBytrainItem(TrainItem trainItem);
	
}