/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.jeeplus.modules.rzzk.dao.TrainPhysicalDao;

/**
 * 体能训练项Service
 * @author shenming
 * @version 2019-03-25
 */
@Service
@Transactional(readOnly = true)
public class TrainPhysicalService extends CrudService<TrainPhysicalDao, TrainPhysical> {

	public TrainPhysical get(String id) {
		return super.get(id);
	}
	
	public List<TrainPhysical> findList(TrainPhysical trainPhysical) {
		return super.findList(trainPhysical);
	}
	
	public Page<TrainPhysical> findPage(Page<TrainPhysical> page, TrainPhysical trainPhysical) {
		return super.findPage(page, trainPhysical);
	}
	
	@Transactional(readOnly = false)
	public void save(TrainPhysical trainPhysical) {
		super.save(trainPhysical);
	}
	
	@Transactional(readOnly = false)
	public void delete(TrainPhysical trainPhysical) {
		super.delete(trainPhysical);
	}
	
	
	
	
}