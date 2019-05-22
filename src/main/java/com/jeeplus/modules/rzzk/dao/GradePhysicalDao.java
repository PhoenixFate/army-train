/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.dao;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import java.util.List;
import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.rzzk.entity.GradePhysical;

/**
 * 体能训练成绩DAO接口
 * @author shenming
 * @version 2019-04-10
 */
@MyBatisDao
public interface GradePhysicalDao extends CrudDao<GradePhysical> {

	public List<Trainee> findListBytrainee(Trainee trainee);
	public List<TrainPhysical> findListBytrainPhysical(TrainPhysical trainPhysical);
	
}