/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jeeplus.modules.rzzk.service.TrainPhysicalService;
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
import com.jeeplus.modules.mycharts.entity.GradePhysical2;
import com.jeeplus.modules.mycharts.service.GradePhysicalService2;

/**
 * 体能训练雷达图Controller
 * @author shenming
 * @version 2019-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/mycharts/GradePhysical2")
public class GradePhysicalController2 extends BaseController {
	
	
	
	private List<Map<String,Object>> orientData;
	@Autowired
	private GradePhysicalService2 GradePhysicalService2;
	
	@Autowired
	private TrainPhysicalService trainPhysicalService;
	
	@ModelAttribute
	public GradePhysical2 get(@RequestParam(required=false) String id) {
		GradePhysical2 entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = GradePhysicalService2.get(id);
		}
		if (entity == null){
			entity = new GradePhysical2();
		}
		return entity;
	}
	
	/**
	 * 体能训练雷达图列表页面
	 */
	@RequiresPermissions("mycharts:GradePhysical2:list")
	@RequestMapping(value = {"list", ""})	
	public String list(GradePhysical2 gradePhysical2, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<GradePhysical2> page = GradePhysicalService2.findPage(new Page<GradePhysical2>(request, response), gradePhysical2); 
		model.addAttribute("page", page);
		
//		if(gradePhysical2!=null && gradePhysical2.getId()!=null && gradePhysical2.getDate()!=null) {
			List<GradePhysical2> list = page.getList();
			List<TrainPhysical> findList = trainPhysicalService.findList(new TrainPhysical());
			System.out.println(findList);
			Double[] dataArr1=new Double[findList.size()];
			for(int i=0;i<findList.size();++i) {
				String name1=findList.get(i).getName();
				for(int j=0;j<list.size();++j) {
					String name2 = list.get(j).getTrainPhysical().getName();
					if(name1.equals(name2)) {
						dataArr1[i]=Double.valueOf(list.get(j).getScore());
					}
				}
				
			}
			orientData = new ArrayList<Map<String,Object>>();
			//Double[] dataArr1 = new Double[]{0.1*100,0.2*100,0.3*100,0.1*100,0.05*100,0.05*100,0.1*100,0.1*100};
			Map<String,Object> mapData1 = new HashMap<String,Object>();
			mapData1.put("dataArr", dataArr1);
			mapData1.put("title", "各项分数");
			orientData.add(mapData1);
			request.setAttribute("orientData", orientData);
			System.out.println("各项分数: "+Arrays.toString(dataArr1));
//		}

		return "modules/mycharts/gradePhysical2List";
	}

	/**
	 * 查看，增加，编辑体能训练雷达图表单页面
	 */
	@RequiresPermissions(value={"mycharts:GradePhysical2:view","mycharts:GradePhysical2:add","mycharts:GradePhysical2:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(GradePhysical2 GradePhysical2, Model model) {
		model.addAttribute("GradePhysical2", GradePhysical2);
		return "modules/mycharts/gradePhysical2Form";
	}

	/**
	 * 保存体能训练雷达图
	 */
	@RequiresPermissions(value={"mycharts:GradePhysical2:add","mycharts:GradePhysical2:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(GradePhysical2 GradePhysical2, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, GradePhysical2)){
			return form(GradePhysical2, model);
		}
		if(!GradePhysical2.getIsNewRecord()){//编辑表单保存
			GradePhysical2 t = GradePhysicalService2.get(GradePhysical2.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(GradePhysical2, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			GradePhysicalService2.save(t);//保存
		}else{//新增表单保存
			GradePhysicalService2.save(GradePhysical2);//保存
		}
		addMessage(redirectAttributes, "保存体能训练雷达图成功");
		return "redirect:"+Global.getAdminPath()+"/mycharts/GradePhysical2/?repage";
	}
	
	/**
	 * 删除体能训练雷达图
	 */
	@RequiresPermissions("mycharts:GradePhysical2:del")
	@RequestMapping(value = "delete")
	public String delete(GradePhysical2 GradePhysical2, RedirectAttributes redirectAttributes) {
		GradePhysicalService2.delete(GradePhysical2);
		addMessage(redirectAttributes, "删除体能训练雷达图成功");
		return "redirect:"+Global.getAdminPath()+"/mycharts/GradePhysical2/?repage";
	}
	
	/**
	 * 批量删除体能训练雷达图
	 */
	@RequiresPermissions("mycharts:GradePhysical2:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			GradePhysicalService2.delete(GradePhysicalService2.get(id));
		}
		addMessage(redirectAttributes, "删除体能训练雷达图成功");
		return "redirect:"+Global.getAdminPath()+"/mycharts/GradePhysical2/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("mycharts:GradePhysical2:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(GradePhysical2 GradePhysical2, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "体能训练雷达图"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<GradePhysical2> page = GradePhysicalService2.findPage(new Page<GradePhysical2>(request, response, -1), GradePhysical2);
    		new ExportExcel("体能训练雷达图", GradePhysical2.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出体能训练雷达图记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mycharts/GradePhysical2/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("mycharts:GradePhysical2:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GradePhysical2> list = ei.getDataList(GradePhysical2.class);
			for (GradePhysical2 GradePhysical2 : list){
				try{
					GradePhysicalService2.save(GradePhysical2);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条体能训练雷达图记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条体能训练雷达图记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入体能训练雷达图失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mycharts/GradePhysical2/?repage";
    }
	
	/**
	 * 下载导入体能训练雷达图数据模板
	 */
	@RequiresPermissions("mycharts:GradePhysical2:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "体能训练雷达图数据导入模板.xlsx";
    		List<GradePhysical2> list = Lists.newArrayList(); 
    		new ExportExcel("体能训练雷达图数据", GradePhysical2.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/mycharts/GradePhysical2/?repage";
    }
	
	
	/**
	 * 选择学员
	 */
	@RequestMapping(value = "selecttrainee")
	public String selecttrainee(Trainee trainee, String url, String fieldLabels, String fieldKeys, String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trainee> page = GradePhysicalService2.findPageBytrainee(new Page<Trainee>(request, response),  trainee);
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
		Page<TrainPhysical> page = GradePhysicalService2.findPageBytrainPhysical(new Page<TrainPhysical>(request, response),  trainPhysical);
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