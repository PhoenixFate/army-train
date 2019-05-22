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
import com.jeeplus.modules.rzzk.entity.MyTree;
import com.jeeplus.modules.rzzk.service.MyTreeService;

/**
 * 树测试Controller
 * @author shenming
 * @version 2019-03-19
 */
@Controller
@RequestMapping(value = "${adminPath}/rzzk/myTree")
public class MyTreeController extends BaseController {

	@Autowired
	private MyTreeService myTreeService;
	
	@ModelAttribute
	public MyTree get(@RequestParam(required=false) String id) {
		MyTree entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = myTreeService.get(id);
		}
		if (entity == null){
			entity = new MyTree();
		}
		return entity;
	}
	
	/**
	 * 树测试列表页面
	 */
	@RequiresPermissions("rzzk:myTree:list")
	@RequestMapping(value = {"list", ""})
	public String list(MyTree myTree, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<MyTree> list = myTreeService.findList(myTree); 
		model.addAttribute("list", list);
		return "modules/rzzk/myTreeList";
	}

	/**
	 * 查看，增加，编辑树测试表单页面
	 */
	@RequiresPermissions(value={"rzzk:myTree:view","rzzk:myTree:add","rzzk:myTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(MyTree myTree, Model model) {
		if (myTree.getParent()!=null && StringUtils.isNotBlank(myTree.getParent().getId())){
			myTree.setParent(myTreeService.get(myTree.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(myTree.getId())){
				MyTree myTreeChild = new MyTree();
				myTreeChild.setParent(new MyTree(myTree.getParent().getId()));
				List<MyTree> list = myTreeService.findList(myTree); 
				if (list.size() > 0){
					myTree.setSort(list.get(list.size()-1).getSort());
					if (myTree.getSort() != null){
						myTree.setSort(myTree.getSort() + 30);
					}
				}
			}
		}
		if (myTree.getSort() == null){
			myTree.setSort(30);
		}
		model.addAttribute("myTree", myTree);
		return "modules/rzzk/myTreeForm";
	}

	/**
	 * 保存树测试
	 */
	@RequiresPermissions(value={"rzzk:myTree:add","rzzk:myTree:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(MyTree myTree, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, myTree)){
			return form(myTree, model);
		}
		if(!myTree.getIsNewRecord()){//编辑表单保存
			MyTree t = myTreeService.get(myTree.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(myTree, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			myTreeService.save(t);//保存
		}else{//新增表单保存
			myTreeService.save(myTree);//保存
		}
		addMessage(redirectAttributes, "保存树测试成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/myTree/?repage";
	}
	
	/**
	 * 删除树测试
	 */
	@RequiresPermissions("rzzk:myTree:del")
	@RequestMapping(value = "delete")
	public String delete(MyTree myTree, RedirectAttributes redirectAttributes) {
		myTreeService.delete(myTree);
		addMessage(redirectAttributes, "删除树测试成功");
		return "redirect:"+Global.getAdminPath()+"/rzzk/myTree/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<MyTree> list = myTreeService.findList(new MyTree());
		for (int i=0; i<list.size(); i++){
			MyTree e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}