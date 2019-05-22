/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.jeeplus.modules.rzzk.entity.Trainee;

/**
 * 体能训练雷达图Entity
 * @author shenming
 * @version 2019-04-18
 */
public class GradePhysical2 extends DataEntity<GradePhysical2> {
	
	private static final long serialVersionUID = 1L;
	private Trainee trainee;		// 学员
	private String score;		// 分数
	private TrainPhysical trainPhysical;		// 体能训练项
	private Date date;		// 日期
	
	public GradePhysical2() {
		super();
	}

	public GradePhysical2(String id){
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
	
	@NotNull(message="体能训练项不能为空")
	@ExcelField(title="体能训练项", align=2, sort=8)
	public TrainPhysical getTrainPhysical() {
		return trainPhysical;
	}

	public void setTrainPhysical(TrainPhysical trainPhysical) {
		this.trainPhysical = trainPhysical;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="日期不能为空")
	@ExcelField(title="日期", align=2, sort=9)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}