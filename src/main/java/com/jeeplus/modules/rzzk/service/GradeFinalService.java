/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.ArmyItem;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.GradeFinal;
import com.jeeplus.modules.rzzk.dao.GradeFinalDao;

/**
 * 考核成绩Service
 * @author shengming
 * @version 2019-04-10
 */
@Service
@Transactional(readOnly = true)
public class GradeFinalService extends CrudService<GradeFinalDao, GradeFinal> {

	public GradeFinal get(String id) {
		return super.get(id);
	}
	
	public List<GradeFinal> findList(GradeFinal gradeFinal) {
		return super.findList(gradeFinal);
	}
	
	public Page<GradeFinal> findPage(Page<GradeFinal> page, GradeFinal gradeFinal) {
		return super.findPage(page, gradeFinal);
	}
	
	@Transactional(readOnly = false)
	public void save(GradeFinal gradeFinal) {
		super.save(gradeFinal);
	}
	
	@Transactional(readOnly = false)
	public void delete(GradeFinal gradeFinal) {
		super.delete(gradeFinal);
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
	public Page<ArmyItem> findPageByarmyItem(Page<ArmyItem> page, ArmyItem armyItem) {
		armyItem.setPage(page);
		page.setList(dao.findListByarmyItem(armyItem));
		return page;
	}
	
	
	
}