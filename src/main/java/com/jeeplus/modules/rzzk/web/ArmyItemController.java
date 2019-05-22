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
import com.jeeplus.modules.rzzk.entity.ArmyItem;
import com.jeeplus.modules.rzzk.service.ArmyItemService;

/**
 * 学期Controller
 * @author shenming
 * @version 2019-04-10
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/armyItem")
public class ArmyItemController extends BaseController {

	@Autowired
	private ArmyItemService armyItemService;
	
	@ModelAttribute
	public ArmyItem get(@RequestParam(required=false) String id) {
		ArmyItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = armyItemService.get(id);
		}
		if (entity == null){
			entity = new ArmyItem();
		}
		return entity;
	}
	
	/**
	 * 学期列表页面
	 */
	@RequiresPermissions("rzzk:armyItem:list")
	@RequestMapping(value = {"list", ""})
	public String list(ArmyItem armyItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ArmyItem> page = armyItemService.findPage(new Page<ArmyItem>(request, response), armyItem); 
		model.addAttribute("page", page);
		return "modules/rzzk/armyItemList";
	}

	/**
	 * 查看，增加，编辑学期表单页面
	 */
	@RequiresPermissions(value={"rzzk:armyItem:view","rzzk:armyItem:add","rzzk:armyItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ArmyItem armyItem, Model model) {
		model.addAttribute("armyItem", armyItem);
		return "modules/rzzk/armyItemForm";
	}

	/**
	 * 保存学期
	 */
	@RequiresPermissions(value={"rzzk:armyItem:add","rzzk:armyItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ArmyItem armyItem, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, armyItem)){
			return form(armyItem, model);
		}
		if(!armyItem.getIsNewRecord()){//编辑表单保存
			ArmyItem t = armyItemService.get(armyItem.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(armyItem, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			armyItemService.save(t);//保存
		}else{//新增表单保存
			armyItemService.save(armyItem);//保存
		}
		addMessage(redirectAttributes, "保存学期成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyItem/?repage";
	}
	
	/**
	 * 删除学期
	 */
	@RequiresPermissions("rzzk:armyItem:del")
	@RequestMapping(value = "delete")
	public String delete(ArmyItem armyItem, RedirectAttributes redirectAttributes) {
		armyItemService.delete(armyItem);
		addMessage(redirectAttributes, "删除学期成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyItem/?repage";
	}
	
	/**
	 * 批量删除学期
	 */
	@RequiresPermissions("rzzk:armyItem:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			armyItemService.delete(armyItemService.get(id));
		}
		addMessage(redirectAttributes, "删除学期成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyItem/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:armyItem:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(ArmyItem armyItem, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学期"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ArmyItem> page = armyItemService.findPage(new Page<ArmyItem>(request, response, -1), armyItem);
    		new ExportExcel("学期", ArmyItem.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出学期记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyItem/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:armyItem:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ArmyItem> list = ei.getDataList(ArmyItem.class);
			for (ArmyItem armyItem : list){
				try{
					armyItemService.save(armyItem);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条学期记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条学期记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入学期失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyItem/?repage";
    }
	
	/**
	 * 下载导入学期数据模板
	 */
	@RequiresPermissions("rzzk:armyItem:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "学期数据导入模板.xlsx";
    		List<ArmyItem> list = Lists.newArrayList(); 
    		new ExportExcel("学期数据", ArmyItem.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyItem/?repage";
    }
	
	
	

}