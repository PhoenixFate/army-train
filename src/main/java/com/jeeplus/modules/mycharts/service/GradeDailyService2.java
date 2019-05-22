/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.mycharts.dao.GradeDailyDao2;
import com.jeeplus.modules.mycharts.entity.GradeDaily2;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.Trainee;

/**
 * 日常训练统计图Service
 * @author shenming
 * @version 2019-04-26
 */
@Service
@Transactional(readOnly = true)
public class GradeDailyService2 extends CrudService<GradeDailyDao2, GradeDaily2> {

	public GradeDaily2 get(String id) {
		return super.get(id);
	}
	
	public List<GradeDaily2> findList(GradeDaily2 gradeDaily) {
		return super.findList(gradeDaily);
	}
	
	public Page<GradeDaily2> findPage(Page<GradeDaily2> page, GradeDaily2 gradeDaily) {
		return super.findPage(page, gradeDaily);
	}
	
	@Transactional(readOnly = false)
	public void save(GradeDaily2 gradeDaily) {
		super.save(gradeDaily);
	}
	
	@Transactional(readOnly = false)
	public void delete(GradeDaily2 gradeDaily) {
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