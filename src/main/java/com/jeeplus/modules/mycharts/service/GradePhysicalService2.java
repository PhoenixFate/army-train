/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.mycharts.entity.GradePhysical2;
import com.jeeplus.modules.mycharts.dao.GradePhysicalDao2;

/**
 * 体能训练雷达图Service
 * @author shenming
 * @version 2019-04-18
 */
@Service
@Transactional(readOnly = true)
public class GradePhysicalService2 extends CrudService<GradePhysicalDao2, GradePhysical2> {

	public GradePhysical2 get(String id) {
		return super.get(id);
	}
	
	public List<GradePhysical2> findList(GradePhysical2 GradePhysical2) {
		return super.findList(GradePhysical2);
	}
	
	public Page<GradePhysical2> findPage(Page<GradePhysical2> page, GradePhysical2 GradePhysical2) {
		return super.findPage(page, GradePhysical2);
	}
	
	@Transactional(readOnly = false)
	public void save(GradePhysical2 GradePhysical2) {
		super.save(GradePhysical2);
	}
	
	@Transactional(readOnly = false)
	public void delete(GradePhysical2 GradePhysical2) {
		super.delete(GradePhysical2);
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