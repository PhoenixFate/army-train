/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.dao;

import java.util.List;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.mycharts.entity.GradePhysical2;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.jeeplus.modules.rzzk.entity.Trainee;

/**
 * 体能训练雷达图DAO接口
 * @author shenming
 * @version 2019-04-18
 */
@MyBatisDao
public interface GradePhysicalDao2 extends CrudDao<GradePhysical2> {

	public List<Trainee> findListBytrainee(Trainee trainee);
	public List<TrainPhysical> findListBytrainPhysical(TrainPhysical trainPhysical);
	
}