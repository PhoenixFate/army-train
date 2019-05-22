/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.mycharts.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
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

import com.google.common.collect.Lists;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.modules.echarts.entity.ChinaWeatherDataBean;
import com.jeeplus.modules.echarts.service.ChinaWeatherDataBeanService;
import com.jeeplus.modules.mycharts.entity.GradeDaily2;
import com.jeeplus.modules.mycharts.service.GradeDailyService2;
import com.jeeplus.modules.rzzk.entity.GradeDaily;
import com.jeeplus.modules.rzzk.entity.TrainItem;
import com.jeeplus.modules.rzzk.entity.Trainee;

/**
 * 日常训练统计图Controller
 * 
 * @author shenming
 * @version 2019-04-26
 */
@Controller
@RequestMapping(value = "${adminPath}/mycharts/gradeDaily")
public class GradeDailyController2 extends BaseController {

	@Autowired
	private GradeDailyService2 gradeDailyService;

	@Autowired
	private ChinaWeatherDataBeanService chinaWeatherDataBeanService;
	
	@ModelAttribute
	public GradeDaily2 get(@RequestParam(required = false) String id) {
		GradeDaily2 entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = gradeDailyService.get(id);
		}
		if (entity == null) {
			entity = new GradeDaily2();
		}
		return entity;
	}

	/**
	 * 日常训练统计图列表页面
	 */
	@RequiresPermissions("mycharts:gradeDaily:list")
	@RequestMapping(value = { "list", "" })
	public String list(GradeDaily2 gradeDaily, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("------------------------------日常训练统计图----------------------------------------");
		Page<GradeDaily2> page = gradeDailyService.findPage(new Page<GradeDaily2>(request, response), gradeDaily);
		model.addAttribute("page", page);

		// X轴的数据
		List<String> xAxisData = new ArrayList<String>();
		
//		xAxisData.add("2019-01-01");
//		xAxisData.add("2019-02-01");
//		xAxisData.add("2019-03-01");
//		xAxisData.add("2019-04-01");
//		xAxisData.add("2019-05-01");
//		xAxisData.add("2019-06-01");
		
		
		// Y轴的数据
		Map<String, List<Double>> yAxisData = new HashMap<String, List<Double>>();
		// Y轴双轴情况下的位置定位
		Map<String, Integer> yAxisIndex = new HashMap<String, Integer>();

		ChinaWeatherDataBean chinaWeatherDataBean=new ChinaWeatherDataBean();
		List<ChinaWeatherDataBean> weatherDataList = chinaWeatherDataBeanService.findList(chinaWeatherDataBean);

		List<Double> beijingMaxTemp = new ArrayList<Double>();
		List<Double> beijingMinTemp = new ArrayList<Double>();
		List<Double> changchunMaxTemp = new ArrayList<Double>();
		List<Double> changchunMinTemp = new ArrayList<Double>();
		List<Double> shenyangMaxTemp = new ArrayList<Double>();
		List<Double> shenyangMinTemp = new ArrayList<Double>();
		List<Double> haerbinMaxTemp = new ArrayList<Double>();
		List<Double> haerbinMinTemp = new ArrayList<Double>();

		for (ChinaWeatherDataBean chinaWeatherDataBeanTemp : weatherDataList) {
			// x轴数据
			xAxisData.add(chinaWeatherDataBeanTemp.getDatestr().toString());
			// 北京最高温度
			beijingMaxTemp.add(chinaWeatherDataBeanTemp.getBeijingMaxTemp());
			// 北京最低温度
			beijingMinTemp.add(chinaWeatherDataBeanTemp.getBeijingMinTemp());
			// 长春最高温度
			changchunMaxTemp.add(chinaWeatherDataBeanTemp.getChangchunMaxTemp());
			// 长春最高温度
			changchunMinTemp.add(chinaWeatherDataBeanTemp.getChangchunMinTemp());
			// 沈阳最高温度
			shenyangMaxTemp.add(chinaWeatherDataBeanTemp.getShenyangMaxTemp());
			// 沈阳最高温度
			shenyangMinTemp.add(chinaWeatherDataBeanTemp.getShenyangMinTemp());
			// 哈尔滨最高温度
			haerbinMaxTemp.add(chinaWeatherDataBeanTemp.getHaerbinMaxTemp());
			// 哈尔滨最高温度
			haerbinMinTemp.add(chinaWeatherDataBeanTemp.getHaerbinMinTemp());
		}

		// y轴数据
		yAxisData.put("北京 最高温度", beijingMaxTemp);
		yAxisData.put("北京 最低温度", beijingMinTemp);
		yAxisData.put("长春 最高温度", changchunMaxTemp);
		yAxisData.put("长春 最低温度", changchunMinTemp);
		yAxisData.put("沈阳 最高温度", shenyangMaxTemp);
		yAxisData.put("沈阳 最低温度", shenyangMinTemp);
		yAxisData.put("哈尔滨 最高温度", haerbinMinTemp);
		yAxisData.put("哈尔滨 最低温度", haerbinMinTemp);

//		List<Double> aList=new ArrayList<Double>();
//		aList.add(10.0);		
//		aList.add(20.0);
//		yAxisData.put("a",aList);
		
		// Y轴双轴情况下的位置定位
		yAxisIndex.put("北京 最高温度", 0);// 0表示Y轴左轴
		yAxisIndex.put("长春 最高温度", 0);// 0表示Y轴左轴
		yAxisIndex.put("沈阳 最高温度", 0);// 0表示Y轴左轴
		yAxisIndex.put("哈尔滨 最高温度", 0);// 0表示Y轴左轴
		yAxisIndex.put("北京 最低温度", 1);// 1表示Y轴右轴
		yAxisIndex.put("长春 最低温度", 1);// 1表示Y轴右轴
		yAxisIndex.put("沈阳 最低温度", 1);// 1表示Y轴右轴
		yAxisIndex.put("哈尔滨 最低温度", 1);// 1表示Y轴右轴

		request.setAttribute("yAxisIndex", yAxisIndex);
		request.setAttribute("xAxisData", xAxisData);
		request.setAttribute("yAxisData", yAxisData);

		return "modules/mycharts/gradeDailyList";
	}

	/**
	 * 查看，增加，编辑日常训练统计图表单页面
	 */
	@RequiresPermissions(value = { "mycharts:gradeDaily:view", "mycharts:gradeDaily:add",
			"mycharts:gradeDaily:edit" }, logical = Logical.OR)
	@RequestMapping(value = "form")
	public String form(GradeDaily2 gradeDaily, Model model) {
		model.addAttribute("gradeDaily", gradeDaily);
		return "modules/mycharts/gradeDailyForm";
	}

	/**
	 * 保存日常训练统计图
	 */
	@RequiresPermissions(value = { "mycharts:gradeDaily:add", "mycharts:gradeDaily:edit" }, logical = Logical.OR)
	@RequestMapping(value = "save")
	public String save(GradeDaily2 gradeDaily, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (!beanValidator(model, gradeDaily)) {
			return form(gradeDaily, model);
		}
		if (!gradeDaily.getIsNewRecord()) {// 编辑表单保存
			GradeDaily2 t = gradeDailyService.get(gradeDaily.getId());// 从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(gradeDaily, t);// 将编辑表单中的非NULL值覆盖数据库记录中的值
			gradeDailyService.save(t);// 保存
		} else {// 新增表单保存
			gradeDailyService.save(gradeDaily);// 保存
		}
		addMessage(redirectAttributes, "保存日常训练统计图成功");
		return "redirect:" + Global.getAdminPath() + "/mycharts/gradeDaily/?repage";
	}

	/**
	 * 删除日常训练统计图
	 */
	@RequiresPermissions("mycharts:gradeDaily:del")
	@RequestMapping(value = "delete")
	public String delete(GradeDaily2 gradeDaily, RedirectAttributes redirectAttributes) {
		gradeDailyService.delete(gradeDaily);
		addMessage(redirectAttributes, "删除日常训练统计图成功");
		return "redirect:" + Global.getAdminPath() + "/mycharts/gradeDaily/?repage";
	}

	/**
	 * 批量删除日常训练统计图
	 */
	@RequiresPermissions("mycharts:gradeDaily:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] = ids.split(",");
		for (String id : idArray) {
			gradeDailyService.delete(gradeDailyService.get(id));
		}
		addMessage(redirectAttributes, "删除日常训练统计图成功");
		return "redirect:" + Global.getAdminPath() + "/mycharts/gradeDaily/?repage";
	}

	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("mycharts:gradeDaily:export")
	@RequestMapping(value = "export", method = RequestMethod.POST)
	public String exportFile(GradeDaily2 gradeDaily, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		try {
			String fileName = "日常训练统计图" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<GradeDaily2> page = gradeDailyService.findPage(new Page<GradeDaily2>(request, response, -1),
					gradeDaily);
			new ExportExcel("日常训练统计图", GradeDaily.class).setDataList(page.getList()).write(response, fileName)
					.dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出日常训练统计图记录失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/mycharts/gradeDaily/?repage";
	}

	/**
	 * 导入Excel数据
	 * 
	 */
	@RequiresPermissions("mycharts:gradeDaily:import")
	@RequestMapping(value = "import", method = RequestMethod.POST)
	public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<GradeDaily2> list = ei.getDataList(GradeDaily2.class);
			for (GradeDaily2 gradeDaily : list) {
				try {
					gradeDailyService.save(gradeDaily);
					successNum++;
				} catch (ConstraintViolationException ex) {
					failureNum++;
				} catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum > 0) {
				failureMsg.insert(0, "，失败 " + failureNum + " 条日常训练统计图记录。");
			}
			addMessage(redirectAttributes, "已成功导入 " + successNum + " 条日常训练统计图记录" + failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入日常训练统计图失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/mycharts/gradeDaily/?repage";
	}

	/**
	 * 下载导入日常训练统计图数据模板
	 */
	@RequiresPermissions("mycharts:gradeDaily:import")
	@RequestMapping(value = "import/template")
	public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
			String fileName = "日常训练统计图数据导入模板.xlsx";
			List<GradeDaily2> list = Lists.newArrayList();
			new ExportExcel("日常训练统计图数据", GradeDaily2.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
		}
		return "redirect:" + Global.getAdminPath() + "/mycharts/gradeDaily/?repage";
	}

	/**
	 * 选择学员
	 */
	@RequestMapping(value = "selecttrainee")
	public String selecttrainee(Trainee trainee, String url, String fieldLabels, String fieldKeys, String searchLabel,
			String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Trainee> page = gradeDailyService.findPageBytrainee(new Page<Trainee>(request, response), trainee);
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
	public String selecttrainItem(TrainItem trainItem, String url, String fieldLabels, String fieldKeys,
			String searchLabel, String searchKey, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<TrainItem> page = gradeDailyService.findPageBytrainItem(new Page<TrainItem>(request, response), trainItem);
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