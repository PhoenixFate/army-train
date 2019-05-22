/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;

import com.jeeplus.modules.rzzk.entity.Trainee;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.ArmyItem;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 考核成绩Entity
 * @author shengming
 * @version 2019-04-10
 */
public class GradeFinal extends DataEntity<GradeFinal> {
	
	private static final long serialVersionUID = 1L;
	private Trainee trainee;		// 学员
	private String score;		// 分数
	private TrainItem trainItem;		// 训练科目
	private ArmyItem armyItem;		// 学期
	
	public GradeFinal() {
		super();
	}

	public GradeFinal(String id){
		super(id);
	}

	@NotNull(message="学员不能为空")
	@ExcelField(title="学员", align=2, sort=6)
	public Trainee getTrainee() {
		return trainee;
	}

	public void setTrainee(Trainee trainee) {
		this.trainee = trainee;
	}
	
	@ExcelField(title="分数", align=2, sort=7)
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@NotNull(message="训练科目不能为空")
	@ExcelField(title="训练科目", align=2, sort=8)
	public TrainItem getTrainItem() {
		return trainItem;
	}

	public void setTrainItem(TrainItem trainItem) {
		this.trainItem = trainItem;
	}
	
	@NotNull(message="学期不能为空")
	@ExcelField(title="学期", align=2, sort=9)
	public ArmyItem getArmyItem() {
		return armyItem;
	}

	public void setArmyItem(ArmyItem armyItem) {
		this.armyItem = armyItem;
	}
	
}