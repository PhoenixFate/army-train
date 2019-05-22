/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.dao.TrainItemDao;

/**
 * 训练科目Service
 * @author shenming
 * @version 2019-03-25
 */
@Service
@Transactional(readOnly = true)
public class TrainItemService extends CrudService<TrainItemDao, TrainItem> {

	public TrainItem get(String id) {
		return super.get(id);
	}
	
	public List<TrainItem> findList(TrainItem trainItem) {
		return super.findList(trainItem);
	}
	
	public Page<TrainItem> findPage(Page<TrainItem> page, TrainItem trainItem) {
		return super.findPage(page, trainItem);
	}
	
	@Transactional(readOnly = false)
	public void save(TrainItem trainItem) {
		super.save(trainItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(TrainItem trainItem) {
		super.delete(trainItem);
	}
	
	
	
	
}