/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.GradeDaily;
import com.jeeplus.modules.rzzk.dao.GradeDailyDao;

/**
 * 日常训练成绩Service
 * @author shenming
 * @version 2019-03-25
 */
@Service
@Transactional(readOnly = true)
public class GradeDailyService extends CrudService<GradeDailyDao, GradeDaily> {

	public GradeDaily get(String id) {
		return super.get(id);
	}
	
	public List<GradeDaily> findList(GradeDaily gradeDaily) {
		return super.findList(gradeDaily);
	}
	
	public Page<GradeDaily> findPage(Page<GradeDaily> page, GradeDaily gradeDaily) {
		return super.findPage(page, gradeDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(GradeDaily gradeDaily) {
		super.save(gradeDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(GradeDaily gradeDaily) {
		super.delete(gradeDaily);
	}
	
	public Page<Trainee> findPageBytrainee(Page<Trainee> page, Trainee trainee) {
		trainee.setPage(page);
		page.setList(dao.findListBytrainee(trainee));
		return page;
	}
	public Page<TrainItem> findPageBytrainItem(Page<TrainItem> page, TrainItem trainItem) {
		trainItem.setPage(page);
		page.setList(dao.findListBytrainItem(trainItem));
		return page;
	}
	
	
	
}