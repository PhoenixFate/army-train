<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>学员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>学员列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="trainee" action="${ctx}/rzzk/trainee/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>姓名：</span>
				<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>性别：</span>
				<form:select path="sex"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>所属单位：</span>
				<sys:treeselect id="unit" name="unit.id" value="${trainee.unit.id}" labelName="unit.name" labelValue="${trainee.unit.name}"
					title="单位" url="/rzzk/armyTree/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
			
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="rzzk:trainee:add">
				<table:addRow url="${ctx}/rzzk/trainee/form" title="学员"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="rzzk:trainee:edit">
			    <table:editRow url="${ctx}/rzzk/trainee/form" title="学员" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="rzzk:trainee:del">
				<table:delRow url="${ctx}/rzzk/trainee/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="rzzk:trainee:import">
				<table:importExcel url="${ctx}/rzzk/trainee/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="rzzk:trainee:export">
	       		<table:exportExcel url="${ctx}/rzzk/trainee/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column name">姓名</th>
				<th  class="sort-column sex">性别</th>
				<th  class="sort-column birthday">出生年月</th>
				<th  class="sort-column unit.name">所属单位</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="trainee">
			<tr>
				<td> <input type="checkbox" id="${trainee.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看学员', '${ctx}/rzzk/trainee/form?id=${trainee.id}','800px', '500px')">
					${trainee.name}
				</a></td>
				<td>
					${fns:getDictLabel(trainee.sex, 'sex', '')}
				</td>
				<td>
					<fmt:formatDate value="${trainee.birthday}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${trainee.unit.name}
				</td>
				<td>
					${trainee.remarks}
				</td>
				<td>
					<shiro:hasPermission name="rzzk:trainee:view">
						<a href="#" onclick="openDialogView('查看学员', '${ctx}/rzzk/trainee/form?id=${trainee.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="rzzk:trainee:edit">
    					<a href="#" onclick="openDialog('修改学员', '${ctx}/rzzk/trainee/form?id=${trainee.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="rzzk:trainee:del">
						<a href="${ctx}/rzzk/trainee/delete?id=${trainee.id}" onclick="return confirmx('确认要删除该学员吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>