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
import com.jeeplus.modules.rzzk.entity.TrainPhysical;
import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.rzzk.entity.GradePhysical;
import com.jeeplus.modules.rzzk.service.GradePhysicalService;

/**
 * 体能训练成绩Controller
 * @author shenming
 * @version 2019-04-10
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/gradePhysical")
public class GradePhysicalController extends BaseController {

	@Autowired
	private GradePhysicalService gradePhysicalService;
	
	@ModelAttribute
	public GradePhysical get(@RequestParam(required=false) String id) {
		GradePhysical entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = gradePhysicalService.get(id);
		}
		if (entity == null){
			entity = new GradePhysical();
		}
		return entity;
	}
	
	/**
	 * 体能训练成绩列表页面
	 */
	@RequiresPermissions("rzzk:gradePhysical:list")
	@RequestMapping(value = {"list", ""})
	public String list(GradePhysical gradePhysical, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GradePhysical> page = gradePhysicalService.findPage(new Page<GradePhysical>(request, response), gradePhysical); 
		model.addAttribute("page", page);
		return "modules/rzzk/gradePhysicalList";
	}

	/**
	 * 查看，增加，编辑体能训练成绩表单页面
	 */
	@RequiresPermissions(value={"rzzk:gradePhysical:view","rzzk:gradePhysical:add","rzzk:gradePhysical:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(GradePhysical gradePhysical, Model model) {
		model.addAttribute("gradePhysical", gradePhysical);
		return "modules/rzzk/gradePhysicalForm";
	}

	/**
	 * 保存体能训练成绩
	 */
	@RequiresPermissions(value={"rzzk:gradePhysical:add","rzzk:gradePhysical:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(GradePhysical gradePhysical, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, gradePhysical)){
			return form(gradePhysical, model);
		}
		if(!gradePhysical.getIsNewRecord()){//编辑表单保存
			GradePhysical t = gradePhysicalService.get(gradePhysical.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(gradePhysical, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			gradePhysicalService.save(t);//保存
		}else{//新增表单保存
			gradePhysicalService.save(gradePhysical);//保存
		}
		addMessage(redirectAttributes, "保存体能训练成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradePhysical/?repage";
	}
	
	/**
	 * 删除体能训练成绩
	 */
	@RequiresPermissions("rzzk:gradePhysical:del")
	@RequestMapping(value = "delete")
	public String delete(GradePhysical gradePhysical, RedirectAttributes redirectAttributes) {
		gradePhysicalService.delete(gradePhysical);
		addMessage(redirectAttributes, "删除体能训练成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradePhysical/?repage";
	}
	
	/**
	 * 批量删除体能训练成绩
	 */
	@RequiresPermissions("rzzk:gradePhysical:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			gradePhysicalService.delete(gradePhysicalService.get(id));
		}
		addMessage(redirectAttributes, "删除体能训练成绩成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradePhysical/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("rzzk:gradePhysical:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(GradePhysical gradePhysical, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "体能训练成绩"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<GradePhysical> page = gradePhysicalService.findPage(new Page<GradePhysical>(request, response, -1), gradePhysical);
    		new ExportExcel("体能训练成绩", GradePhysical.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出体能训练成绩记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradePhysical/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("rzzk:gradePhysical:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GradePhysical> list = ei.getDataList(GradePhysical.class);
			for (GradePhysical gradePhysical : list){
				try{
					gradePhysicalService.save(gradePhysical);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条体能训练成绩记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条体能训练成绩记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入体能训练成绩失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradePhysical/?repage";
    }
	
	/**
	 * 下载导入体能训练成绩数据模板
	 */
	@RequiresPermissions("rzzk:gradePhysical:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "体能训练成绩数据导入模板.xlsx";
    		List<GradePhysical> list = Lists.newArrayList(); 
    		new ExportExcel("体能训练成绩数据", GradePhysical.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/rzzk/gradePhysical/?repage";
    }
	
	
	/**
	 * 选择学员
	 */
	@RequestMapping(value = "selecttrainee")
	public String selecttrainee(Trainee trainee, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trainee> page = gradePhysicalService.findPageBytrainee(new Page<Trainee>(request, response),  trainee);
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
	 * 选择体能训练项
	 */
	@RequestMapping(value = "selecttrainPhysical")
	public String selecttrainPhysical(TrainPhysical trainPhysical, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<TrainPhysical> page = gradePhysicalService.findPageBytrainPhysical(new Page<TrainPhysical>(request, response),  trainPhysical);
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
		model.addAttribute("obj", trainPhysical);
		model.addAttribute("page", page);
		return "modules/sys/gridselect";
	}
	

}