/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.GradePhysical;
import com.jeeplus.modules.rzzk.dao.GradePhysicalDao;

/**
 * 体能训练成绩Service
 * @author shenming
 * @version 2019-04-10
 */
@Service
@Transactional(readOnly = true)
public class GradePhysicalService extends CrudService<GradePhysicalDao, GradePhysical> {

	public GradePhysical get(String id) {
		return super.get(id);
	}
	
	public List<GradePhysical> findList(GradePhysical gradePhysical) {
		return super.findList(gradePhysical);
	}
	
	public Page<GradePhysical> findPage(Page<GradePhysical> page, GradePhysical gradePhysical) {
		return super.findPage(page, gradePhysical);
	}
	
	@Transactional(readOnly = false)
	public void save(GradePhysical gradePhysical) {
		super.save(gradePhysical);
	}
	
	@Transactional(readOnly = false)
	public void delete(GradePhysical gradePhysical) {
		super.delete(gradePhysical);
	}
	
	public Page<Trainee> findPageBytrainee(Page<Trainee> page, Trainee trainee) {
		trainee.setPage(page);
		page.setList(dao.findListBytrainee(trainee));
		return page;
	}
	public Page<TrainPhysical> findPageBytrainPhysical(Page<TrainPhysical> page, TrainPhysical trainPhysical) {
		trainPhysical.setPage(page);
		page.setList(dao.findListBytrainPhysical(trainPhysical));
		return page;
	}
	
	
	
}