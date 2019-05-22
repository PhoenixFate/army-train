/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.rzzk.entity.ArmyItem;
import com.jeeplus.modules.rzzk.dao.ArmyItemDao;

/**
 * 学期Service
 * @author shenming
 * @version 2019-04-10
 */
@Service
@Transactional(readOnly = true)
public class ArmyItemService extends CrudService<ArmyItemDao, ArmyItem> {

	public ArmyItem get(String id) {
		return super.get(id);
	}
	
	public List<ArmyItem> findList(ArmyItem armyItem) {
		return super.findList(armyItem);
	}
	
	public Page<ArmyItem> findPage(Page<ArmyItem> page, ArmyItem armyItem) {
		return super.findPage(page, armyItem);
	}
	
	@Transactional(readOnly = false)
	public void save(ArmyItem armyItem) {
		super.save(armyItem);
	}
	
	@Transactional(readOnly = false)
	public void delete(ArmyItem armyItem) {
		super.delete(armyItem);
	}
	
	
	
	
}