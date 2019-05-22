/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.rzzk.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.rzzk.entity.ArmyTree;
import com.jeeplus.modules.rzzk.service.ArmyTreeService;

/**
 * 部队组织结构Controller
 * @author shengming
 * @version 2019-03-22
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/armyTree")
public class ArmyTreeController extends BaseController {

	@Autowired
	private ArmyTreeService armyTreeService;
	
	@ModelAttribute
	public ArmyTree get(@RequestParam(required=false) String id) {
		ArmyTree entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = armyTreeService.get(id);
		}
		if (entity == null){
			entity = new ArmyTree();
		}
		return entity;
	}
	
	/**
	 * 部队组织结构列表页面
	 */
	@RequiresPermissions("rzzk:armyTree:list")
	@RequestMapping(value = {"list", ""})
	public String list(ArmyTree armyTree, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<ArmyTree> list = armyTreeService.findList(armyTree); 
		model.addAttribute("list", list);
		return "modules/rzzk/armyTreeList";
	}

	/**
	 * 查看，增加，编辑部队组织结构表单页面
	 */
	@RequiresPermissions(value={"rzzk:armyTree:view","rzzk:armyTree:add","rzzk:armyTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(ArmyTree armyTree, Model model) {
		if (armyTree.getParent()!=null && StringUtils.isNotBlank(armyTree.getParent().getId())){
			armyTree.setParent(armyTreeService.get(armyTree.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(armyTree.getId())){
				ArmyTree armyTreeChild = new ArmyTree();
				armyTreeChild.setParent(new ArmyTree(armyTree.getParent().getId()));
				List<ArmyTree> list = armyTreeService.findList(armyTree); 
				if (list.size() > 0){
					armyTree.setSort(list.get(list.size()-1).getSort());
					if (armyTree.getSort() != null){
						armyTree.setSort(armyTree.getSort() + 30);
					}
				}
			}
		}
		if (armyTree.getSort() == null){
			armyTree.setSort(30);
		}
		model.addAttribute("armyTree", armyTree);
		return "modules/rzzk/armyTreeForm";
	}

	/**
	 * 保存部队组织结构
	 */
	@RequiresPermissions(value={"rzzk:armyTree:add","rzzk:armyTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(ArmyTree armyTree, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, armyTree)){
			return form(armyTree, model);
		}
		if(!armyTree.getIsNewRecord()){//编辑表单保存
			ArmyTree t = armyTreeService.get(armyTree.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(armyTree, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			armyTreeService.save(t);//保存
		}else{//新增表单保存
			armyTreeService.save(armyTree);//保存
		}
		addMessage(redirectAttributes, "保存部队组织结构成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyTree/?repage";
	}
	
	/**
	 * 删除部队组织结构
	 */
	@RequiresPermissions("rzzk:armyTree:del")
	@RequestMapping(value = "delete")
	public String delete(ArmyTree armyTree, RedirectAttributes redirectAttributes) {
		armyTreeService.delete(armyTree);
		addMessage(redirectAttributes, "删除部队组织结构成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/armyTree/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, @RequestParam(required=false) String type,
			@RequestParam(required=false) Long grade, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<ArmyTree> list = armyTreeService.findList(new ArmyTree());
		for (int i=0; i<list.size(); i++){
			ArmyTree e = list.get(i);
			if ( (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1))
				&& (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
				&& (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
					
				){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("pIds", e.getParentIds());
				map.put("name", e.getName());
				if (type != null && "3".equals(type)){
					map.put("isParent", true);
				}
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}