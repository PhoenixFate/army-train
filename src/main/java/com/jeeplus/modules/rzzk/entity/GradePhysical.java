/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.entity;

import com.jeeplus.modules.rzzk.entity.Trainee;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 体能训练成绩Entity
 * @author shenming
 * @version 2019-04-10
 */
public class GradePhysical extends DataEntity<GradePhysical> {
	
	private static final long serialVersionUID = 1L;
	private Trainee trainee;		// 学员
	private String score;		// 分数
	private TrainPhysical trainPhysical;		// 体能训练项
	private Date date;		// 日期
	
	public GradePhysical() {
		super();
	}

	public GradePhysical(String id){
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