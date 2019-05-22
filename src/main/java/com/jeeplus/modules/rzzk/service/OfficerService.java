/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.dao.OfficerDao;
import com.jeeplus.modules.rzzk.entity.ArmyTree;
import com.jeeplus.modules.rzzk.entity.Officer;


/**
 * 军官Service
 * @author shenming
 * @version 2019-03-22
 */
@Service
@Transactional(readOnly = true)
public class OfficerService extends CrudService<OfficerDao, Officer> {

	
	public Officer get(String id) {
		Officer officer = super.get(id);
		return officer;
	}
	
	public List<Officer> findList(Officer officer) {
		return super.findList(officer);
	}
	
	public Page<Officer> findPage(Page<Officer> page, Officer officer) {
		return super.findPage(page, officer);
	}
	
	@Transactional(readOnly = false)
	public void save(Officer officer) {
		super.save(officer);
	}
	
	@Transactional(readOnly = false)
	public void delete(Officer officer) {
		super.delete(officer);
	}
	
	public Page<ArmyTree> findPageByunit(Page<ArmyTree> page, ArmyTree unit) {
		unit.setPage(page);
		page.setList(dao.findListByunit(unit));
		return page;
	}
	
}