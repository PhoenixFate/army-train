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
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.jeeplus.modules.rzzk.service.TrainPhysicalService;

/**
 * 体能训练项Controller
 * @author shenming
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/trainPhysical")
public class TrainPhysicalController extends BaseController {

	@Autowired
	private TrainPhysicalService trainPhysicalService;
	
	@ModelAttribute
	public TrainPhysical get(@RequestParam(required=false) String id) {
		TrainPhysical entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = trainPhysicalService.get(id);
		}
		if (entity == null){
			entity = new TrainPhysical();
		}
		return entity;
	}
	
	/**
	 * 体能训练项列表页面
	 */
	@RequiresPermissions("rzzk:trainPhysical:list")
	@RequestMapping(value = {"list", ""})
	public String list(TrainPhysical trainPhysical, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainPhysical> page = trainPhysicalService.findPage(new Page<TrainPhysical>(request, response), trainPhysical); 
		model.addAttribute("page", page);
		return "modules/rzzk/trainPhysicalList";
	}

	/**
	 * 查看，增加，编辑体能训练项表单页面
	 */
	@RequiresPermissions(value={"rzzk:trainPhysical:view","rzzk:trainPhysical:add","rzzk:trainPhysical:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TrainPhysical trainPhysical, Model model) {
		model.addAttribute("trainPhysical", trainPhysical);
		return "modules/rzzk/trainPhysicalForm";
	}

	/**
	 * 保存体能训练项
	 */
	@RequiresPermissions(value={"rzzk:trainPhysical:add","rzzk:trainPhysical:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TrainPhysical trainPhysical, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, trainPhysical)){
			return form(trainPhysical, model);
		}
		if(!trainPhysical.getIsNewRecord()){//编辑表单保存
			TrainPhysical t = trainPhysicalService.get(trainPhysical.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(trainPhysical, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			trainPhysicalService.save(t);//保存
		}else{//新增表单保存
			trainPhysicalService.save(trainPhysical);//保存
		}
		addMessage(redirectAttributes, "保存体能训练项成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainPhysical/?repage";
	}
	
	/**
	 * 删除体能训练项
	 */
	@RequiresPermissions("rzzk:trainPhysical:del")
	@RequestMapping(value = "delete")
	public String delete(TrainPhysical trainPhysical, RedirectAttributes redirectAttributes) {
		trainPhysicalService.delete(trainPhysical);
		addMessage(redirectAttributes, "删除体能训练项成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainPhysical/?repage";
	}
	
	/**
	 * 批量删除体能训练项
	 */
	@RequiresPermissions("rzzk:trainPhysical:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			trainPhysicalService.delete(trainPhysicalService.get(id));
		}
		addMessage(redirectAttributes, "删除体能训练项成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainPhysical/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:trainPhysical:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TrainPhysical trainPhysical, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "体能训练项"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TrainPhysical> page = trainPhysicalService.findPage(new Page<TrainPhysical>(request, response, -1), trainPhysical);
    		new ExportExcel("体能训练项", TrainPhysical.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出体能训练项记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainPhysical/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:trainPhysical:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TrainPhysical> list = ei.getDataList(TrainPhysical.class);
			for (TrainPhysical trainPhysical : list){
				try{
					trainPhysicalService.save(trainPhysical);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条体能训练项记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条体能训练项记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入体能训练项失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainPhysical/?repage";
    }
	
	/**
	 * 下载导入体能训练项数据模板
	 */
	@RequiresPermissions("rzzk:trainPhysical:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "体能训练项数据导入模板.xlsx";
    		List<TrainPhysical> list = Lists.newArrayList(); 
    		new ExportExcel("体能训练项数据", TrainPhysical.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainPhysical/?repage";
    }
	
	
	

}