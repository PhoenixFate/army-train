/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.mycharts.entity.GradeDaily2;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.Trainee;

/**
 * 日常训练统计图DAO接口
 * @author shenming
 * @version 2019-04-26
 */
@MyBatisDao
public interface GradeDailyDao2 extends CrudDao<GradeDaily2> {

	public List<Trainee> findListBytrainee(Trainee trainee);
	public List<TrainItem> findListBytrainItem(TrainItem trainItem);
	
}