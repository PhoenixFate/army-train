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
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.rzzk.entity.GradeDaily;
import com.jeeplus.modules.rzzk.service.GradeDailyService;

/**
 * 日常训练成绩Controller
 * @author shenming
 * @version 2019-03-25
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/gradeDaily")
public class GradeDailyController extends BaseController {

	@Autowired
	private GradeDailyService gradeDailyService;
	
	@ModelAttribute
	public GradeDaily get(@RequestParam(required=false) String id) {
		GradeDaily entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = gradeDailyService.get(id);
		}
		if (entity == null){
			entity = new GradeDaily();
		}
		return entity;
	}
	
	/**
	 * 日常训练成绩列表页面
	 */
	@RequiresPermissions("rzzk:gradeDaily:list")
	@RequestMapping(value = {"list", ""})
	public String list(GradeDaily gradeDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GradeDaily> page = gradeDailyService.findPage(new Page<GradeDaily>(request, response), gradeDaily); 
		model.addAttribute("page", page);
		return "modules/rzzk/gradeDailyList";
	}

	/**
	 * 查看，增加，编辑日常训练成绩表单页面
	 */
	@RequiresPermissions(value={"rzzk:gradeDaily:view","rzzk:gradeDaily:add","rzzk:gradeDaily:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(GradeDaily gradeDaily, Model model) {
		model.addAttribute("gradeDaily", gradeDaily);
		return "modules/rzzk/gradeDailyForm";
	}

	/**
	 * 保存日常训练成绩
	 */
	@RequiresPermissions(value={"rzzk:gradeDaily:add","rzzk:gradeDaily:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(GradeDaily gradeDaily, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, gradeDaily)){
			return form(gradeDaily, model);
		}
		if(!gradeDaily.getIsNewRecord()){//编辑表单保存
			GradeDaily t = gradeDailyService.get(gradeDaily.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(gradeDaily, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			gradeDailyService.save(t);//保存
			
		}else{//新增表单保存
			gradeDailyService.save(gradeDaily);//保存
		}
		addMessage(redirectAttributes, "保存日常训练成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeDaily/?repage";
	}
	
	/**
	 * 删除日常训练成绩
	 */
	@RequiresPermissions("rzzk:gradeDaily:del")
	@RequestMapping(value = "delete")
	public String delete(GradeDaily gradeDaily, RedirectAttributes redirectAttributes) {
		gradeDailyService.delete(gradeDaily);
		addMessage(redirectAttributes, "删除日常训练成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeDaily/?repage";
	}
	
	/**
	 * 批量删除日常训练成绩
	 */
	@RequiresPermissions("rzzk:gradeDaily:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			gradeDailyService.delete(gradeDailyService.get(id));
		}
		addMessage(redirectAttributes, "删除日常训练成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeDaily/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:gradeDaily:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(GradeDaily gradeDaily, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "日常训练成绩"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<GradeDaily> page = gradeDailyService.findPage(new Page<GradeDaily>(request, response, -1), gradeDaily);
    		new ExportExcel("日常训练成绩", GradeDaily.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出日常训练成绩记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeDaily/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:gradeDaily:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GradeDaily> list = ei.getDataList(GradeDaily.class);
			for (GradeDaily gradeDaily : list){
				try{
					gradeDailyService.save(gradeDaily);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条日常训练成绩记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条日常训练成绩记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入日常训练成绩失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeDaily/?repage";
    }
	
	/**
	 * 下载导入日常训练成绩数据模板
	 */
	@RequiresPermissions("rzzk:gradeDaily:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "日常训练成绩数据导入模板.xlsx";
    		List<GradeDaily> list = Lists.newArrayList(); 
    		new ExportExcel("日常训练成绩数据", GradeDaily.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradeDaily/?repage";
    }
	
	
	/**
	 * 选择学员
	 */
	@RequestMapping(value = "selecttrainee")
	public String selecttrainee(Trainee trainee, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trainee> page = gradeDailyService.findPageBytrainee(new Page<Trainee>(request, response),  trainee);
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
		Page<TrainItem> page = gradeDailyService.findPageBytrainItem(new Page<TrainItem>(request, response),  trainItem);
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
	

}