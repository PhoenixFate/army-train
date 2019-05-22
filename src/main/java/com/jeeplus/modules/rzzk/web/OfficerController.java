/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import com.jeeplus.modules.rzzk.entity.ArmyTree;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.rzzk.entity.Officer;
import com.jeeplus.modules.rzzk.service.OfficerService;

/**
 * 军官Controller
 * @author shenming
 * @version 2019-03-22
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/officer")
public class OfficerController extends BaseController {

	@Autowired
	private OfficerService officerService;
	
	@ModelAttribute
	public Officer get(@RequestParam(required=false) String id) {
		Officer entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = officerService.get(id);
		}
		if (entity == null){
			entity = new Officer();
		}
		return entity;
	}
	
	/**
	 * 军官列表页面
	 */
	@RequiresPermissions("rzzk:officer:list")
	@RequestMapping(value = {"list", ""})
	public String list(Officer officer, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Officer> page = officerService.findPage(new Page<Officer>(request, response), officer); 
		model.addAttribute("page", page);
		return "modules/rzzk/officerList";
	}

	/**
	 * 查看，增加，编辑军官表单页面
	 */
	@RequiresPermissions(value={"rzzk:officer:view","rzzk:officer:add","rzzk:officer:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Officer officer, Model model) {
		model.addAttribute("officer", officer);
		return "modules/rzzk/officerForm";
	}

	/**
	 * 保存军官
	 */
	@RequiresPermissions(value={"rzzk:officer:add","rzzk:officer:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Officer officer, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, officer)){
			return form(officer, model);
		}
		if(!officer.getIsNewRecord()){//编辑表单保存
			Officer t = officerService.get(officer.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(officer, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			officerService.save(t);//保存
		}else{//新增表单保存
			officerService.save(officer);//保存
		}
		addMessage(redirectAttributes, "保存军官成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/officer/?repage";
	}
	
	/**
	 * 删除军官
	 */
	@RequiresPermissions("rzzk:officer:del")
	@RequestMapping(value = "delete")
	public String delete(Officer officer, RedirectAttributes redirectAttributes) {
		officerService.delete(officer);
		addMessage(redirectAttributes, "删除军官成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/officer/?repage";
	}
	
	/**
	 * 批量删除军官
	 */
	@RequiresPermissions("rzzk:officer:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			officerService.delete(officerService.get(id));
		}
		addMessage(redirectAttributes, "删除军官成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/officer/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:officer:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Officer officer, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "军官"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Officer> page = officerService.findPage(new Page<Officer>(request, response, -1), officer);
    		new ExportExcel("军官", Officer.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出军官记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/officer/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:officer:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Officer> list = ei.getDataList(Officer.class);
			for (Officer officer : list){
				try{
					officerService.save(officer);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条军官记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条军官记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入军官失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/officer/?repage";
    }
	
	/**
	 * 下载导入军官数据模板
	 */
	@RequiresPermissions("rzzk:officer:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "军官数据导入模板.xlsx";
    		List<Officer> list = Lists.newArrayList(); 
    		new ExportExcel("军官数据", Officer.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/officer/?repage";
    }
	
	
	/**
	 * 选择单位
	 */
	@RequestMapping(value = "selectunit")
	public String selectunit(ArmyTree unit, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ArmyTree> page = officerService.findPageByunit(new Page<ArmyTree>(request, response),  unit);
		try {
			fieldLabels = URLDecoder.decode(fieldLabels, "UTF-8");
			fieldKeys = URLDecoder.decode(fieldKeys, "UTF-8");
			searchLabel = URLDecoder.decode(searchLabel, "UTF-8");
			searchKey = URLDecoder.decode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("labelNames", fieldLabels.split("\\|"));
		model.addAttribute("labelValues", fieldKeys.split("\\|"));
		model.addAttribute("fieldLabels", fieldLabels);
		model.addAttribute("fieldKeys", fieldKeys);
		model.addAttribute("url", url);
		model.addAttribute("searchLabel", searchLabel);
		model.addAttribute("searchKey", searchKey);
		model.addAttribute("obj", unit);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}