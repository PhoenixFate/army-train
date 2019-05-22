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

import com.jeeplus.modules.rzzk.entity.Trainee;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.ArmyItem;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.rzzk.entity.GradeFinal;
import com.jeeplus.modules.rzzk.service.GradeFinalService;

/**
 * 考核成绩Controller
 * @author shengming
 * @version 2019-04-10
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/gradeFinal")
public class GradeFinalController extends BaseController {

	@Autowired
	private GradeFinalService gradeFinalService;
	
	@ModelAttribute
	public GradeFinal get(@RequestParam(required=false) String id) {
		GradeFinal entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = gradeFinalService.get(id);
		}
		if (entity == null){
			entity = new GradeFinal();
		}
		return entity;
	}
	
	/**
	 * 考核成绩列表页面
	 */
	@RequiresPermissions("rzzk:gradeFinal:list")
	@RequestMapping(value = {"list", ""})
	public String list(GradeFinal gradeFinal, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GradeFinal> page = gradeFinalService.findPage(new Page<GradeFinal>(request, response), gradeFinal); 
		model.addAttribute("page", page);
		return "modules/rzzk/gradeFinalList";
	}

	/**
	 * 查看，增加，编辑考核成绩表单页面
	 */
	@RequiresPermissions(value={"rzzk:gradeFinal:view","rzzk:gradeFinal:add","rzzk:gradeFinal:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(GradeFinal gradeFinal, Model model) {
		model.addAttribute("gradeFinal", gradeFinal);
		return "modules/rzzk/gradeFinalForm";
	}

	/**
	 * 保存考核成绩
	 */
	@RequiresPermissions(value={"rzzk:gradeFinal:add","rzzk:gradeFinal:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(GradeFinal gradeFinal, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, gradeFinal)){
			return form(gradeFinal, model);
		}
		if(!gradeFinal.getIsNewRecord()){//编辑表单保存
			GradeFinal t = gradeFinalService.get(gradeFinal.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(gradeFinal, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			gradeFinalService.save(t);//保存
		}else{//新增表单保存
			gradeFinalService.save(gradeFinal);//保存
		}
		addMessage(redirectAttributes, "保存考核成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeFinal/?repage";
	}
	
	/**
	 * 删除考核成绩
	 */
	@RequiresPermissions("rzzk:gradeFinal:del")
	@RequestMapping(value = "delete")
	public String delete(GradeFinal gradeFinal, RedirectAttributes redirectAttributes) {
		gradeFinalService.delete(gradeFinal);
		addMessage(redirectAttributes, "删除考核成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeFinal/?repage";
	}
	
	/**
	 * 批量删除考核成绩
	 */
	@RequiresPermissions("rzzk:gradeFinal:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			gradeFinalService.delete(gradeFinalService.get(id));
		}
		addMessage(redirectAttributes, "删除考核成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeFinal/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:gradeFinal:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(GradeFinal gradeFinal, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考核成绩"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<GradeFinal> page = gradeFinalService.findPage(new Page<GradeFinal>(request, response, -1), gradeFinal);
    		new ExportExcel("考核成绩", GradeFinal.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出考核成绩记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeFinal/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:gradeFinal:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GradeFinal> list = ei.getDataList(GradeFinal.class);
			for (GradeFinal gradeFinal : list){
				try{
					gradeFinalService.save(gradeFinal);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条考核成绩记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条考核成绩记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入考核成绩失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeFinal/?repage";
    }
	
	/**
	 * 下载导入考核成绩数据模板
	 */
	@RequiresPermissions("rzzk:gradeFinal:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "考核成绩数据导入模板.xlsx";
    		List<GradeFinal> list = Lists.newArrayList(); 
    		new ExportExcel("考核成绩数据", GradeFinal.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeFinal/?repage";
    }
	
	
	/**
	 * 选择学员
	 */
	@RequestMapping(value = "selecttrainee")
	public String selecttrainee(Trainee trainee, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trainee> page = gradeFinalService.findPageBytrainee(new Page<Trainee>(request, response),  trainee);
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
		model.addAttribute("obj", trainee);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择训练科目
	 */
	@RequestMapping(value = "selecttrainItem")
	public String selecttrainItem(TrainItem trainItem, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainItem> page = gradeFinalService.findPageBytrainItem(new Page<TrainItem>(request, response),  trainItem);
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
		model.addAttribute("obj", trainItem);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	/**
	 * 选择学期
	 */
	@RequestMapping(value = "selectarmyItem")
	public String selectarmyItem(ArmyItem armyItem, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ArmyItem> page = gradeFinalService.findPageByarmyItem(new Page<ArmyItem>(request, response),  armyItem);
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
		model.addAttribute("obj", armyItem);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}