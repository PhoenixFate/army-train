/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.entity;

import com.jeeplus.modules.rzzk.entity.Trainee;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 日常训练统计图Entity
 * @author shenming
 * @version 2019-04-26
 */
public class GradeDaily2 extends DataEntity<GradeDaily2> {
	
	private static final long serialVersionUID = 1L;
	private Trainee trainee;		// 学员
	private String score;		// 分数
	private TrainItem trainItem;		// 训练科目
	private Date date;		// 日期
	
	public GradeDaily2() {
		super();
	}

	public GradeDaily2(String id){
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