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
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.service.TrainItemService;

/**
 * 训练科目Controller
 * @author shenming
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/trainItem")
public class TrainItemController extends BaseController {

	@Autowired
	private TrainItemService trainItemService;
	
	@ModelAttribute
	public TrainItem get(@RequestParam(required=false) String id) {
		TrainItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = trainItemService.get(id);
		}
		if (entity == null){
			entity = new TrainItem();
		}
		return entity;
	}
	
	/**
	 * 训练科目列表页面
	 */
	@RequiresPermissions("rzzk:trainItem:list")
	@RequestMapping(value = {"list", ""})
	public String list(TrainItem trainItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainItem> page = trainItemService.findPage(new Page<TrainItem>(request, response), trainItem); 
		model.addAttribute("page", page);
		return "modules/rzzk/trainItemList";
	}

	/**
	 * 查看，增加，编辑训练科目表单页面
	 */
	@RequiresPermissions(value={"rzzk:trainItem:view","rzzk:trainItem:add","rzzk:trainItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(TrainItem trainItem, Model model) {
		model.addAttribute("trainItem", trainItem);
		return "modules/rzzk/trainItemForm";
	}

	/**
	 * 保存训练科目
	 */
	@RequiresPermissions(value={"rzzk:trainItem:add","rzzk:trainItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(TrainItem trainItem, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, trainItem)){
			return form(trainItem, model);
		}
		if(!trainItem.getIsNewRecord()){//编辑表单保存
			TrainItem t = trainItemService.get(trainItem.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(trainItem, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			trainItemService.save(t);//保存
		}else{//新增表单保存
			trainItemService.save(trainItem);//保存
		}
		addMessage(redirectAttributes, "保存训练科目成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainItem/?repage";
	}
	
	/**
	 * 删除训练科目
	 */
	@RequiresPermissions("rzzk:trainItem:del")
	@RequestMapping(value = "delete")
	public String delete(TrainItem trainItem, RedirectAttributes redirectAttributes) {
		trainItemService.delete(trainItem);
		addMessage(redirectAttributes, "删除训练科目成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainItem/?repage";
	}
	
	/**
	 * 批量删除训练科目
	 */
	@RequiresPermissions("rzzk:trainItem:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			trainItemService.delete(trainItemService.get(id));
		}
		addMessage(redirectAttributes, "删除训练科目成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainItem/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:trainItem:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(TrainItem trainItem, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "训练科目"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<TrainItem> page = trainItemService.findPage(new Page<TrainItem>(request, response, -1), trainItem);
    		new ExportExcel("训练科目", TrainItem.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出训练科目记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainItem/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:trainItem:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<TrainItem> list = ei.getDataList(TrainItem.class);
			for (TrainItem trainItem : list){
				try{
					trainItemService.save(trainItem);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条训练科目记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条训练科目记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入训练科目失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainItem/?repage";
    }
	
	/**
	 * 下载导入训练科目数据模板
	 */
	@RequiresPermissions("rzzk:trainItem:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "训练科目数据导入模板.xlsx";
    		List<TrainItem> list = Lists.newArrayList(); 
    		new ExportExcel("训练科目数据", TrainItem.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/trainItem/?repage";
    }
	
	
	

}