/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.dao.TraineeDao;

/**
 * 学员Service
 * @author shenming
 * @version 2019-03-25
 */
@Service
@Transactional(readOnly = true)
public class TraineeService extends CrudService<TraineeDao, Trainee> {

	public Trainee get(String id) {
		return super.get(id);
	}
	
	public List<Trainee> findList(Trainee trainee) {
		return super.findList(trainee);
	}
	
	public Page<Trainee> findPage(Page<Trainee> page, Trainee trainee) {
		return super.findPage(page, trainee);
	}
	
	@Transactional(readOnly = false)
	public void save(Trainee trainee) {
		super.save(trainee);
	}
	
	@Transactional(readOnly = false)
	public void delete(Trainee trainee) {
		super.delete(trainee);
	}
	
	
	
	
}