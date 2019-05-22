/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.service.TraineeService;

/**
 * 学员Controller
 * @author shenming
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/trainee")
public class TraineeController extends BaseController {

	@Autowired
	private TraineeService traineeService;
	
	@ModelAttribute
	public Trainee get(@RequestParam(required=false) String id) {
		Trainee entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = traineeService.get(id);
		}
		if (entity == null){
			entity = new Trainee();
		}
		return entity;
	}
	
	/**
	 * 学员列表页面
	 */
	@RequiresPermissions("rzzk:trainee:list")
	@RequestMapping(value = {"list", ""})
	public String list(Trainee trainee, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trainee> page = traineeService.findPage(new Page<Trainee>(request, response), trainee); 
		model.addAttribute("page", page);
		return "modules/rzzk/traineeList";
	}

	/**
	 * 查看，增加，编辑学员表单页面
	 */
	@RequiresPermissions(value={"rzzk:trainee:view","rzzk:trainee:add","rzzk:trainee:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Trainee trainee, Model model) {
		model.addAttribute("trainee", trainee);
		return "modules/rzzk/traineeForm";
	}

	/**
	 * 保存学员
	 */
	@RequiresPermissions(value={"rzzk:trainee:add","rzzk:trainee:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Trainee trainee, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, trainee)){
			return form(trainee, model);
		}
		if(!trainee.getIsNewRecord()){//编辑表单保存
			Trainee t = traineeService.get(trainee.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(trainee, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			traineeService.save(t);//保存
		}else{//新增表单保存
			traineeService.save(trainee);//保存
		}
		addMessage(redirectAttributes, "保存学员成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainee/?repage";
	}
	
	/**
	 * 删除学员
	 */
	@RequiresPermissions("rzzk:trainee:del")
	@RequestMapping(value = "delete")
	public String delete(Trainee trainee, RedirectAttributes redirectAttributes) {
		traineeService.delete(trainee);
		addMessage(redirectAttributes, "删除学员成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainee/?repage";
	}
	
	/**
	 * 批量删除学员
	 */
	@RequiresPermissions("rzzk:trainee:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			traineeService.delete(traineeService.get(id));
		}
		addMessage(redirectAttributes, "删除学员成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainee/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:trainee:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Trainee trainee, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学员"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Trainee> page = traineeService.findPage(new Page<Trainee>(request, response, -1), trainee);
    		new ExportExcel("学员", Trainee.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学员记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainee/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:trainee:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Trainee> list = ei.getDataList(Trainee.class);
			for (Trainee trainee : list){
				try{
					traineeService.save(trainee);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学员记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学员记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学员失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainee/?repage";
    }
	
	/**
	 * 下载导入学员数据模板
	 */
	@RequiresPermissions("rzzk:trainee:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学员数据导入模板.xlsx";
    		List<Trainee> list = Lists.newArrayList(); 
    		new ExportExcel("学员数据", Trainee.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainee/?repage";
    }
	
	
	

}