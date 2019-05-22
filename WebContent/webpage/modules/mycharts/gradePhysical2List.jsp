<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>体能训练雷达图管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#date', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
	</script>
</head>
<body class="gray-bg">
<%@ include file="/webpage/include/echarts.jsp"%>
	<div class="wrapper wrapper-content">
	<div id="radar16"  class="main000"></div>
	<echarts:radar 
		id="radar16"
		title="体能训练统计" 
		subtitle=""
		orientData="${orientData}"
		polarType="16"/>
		
		
	<div class="ibox">
	<div class="ibox-title">
		<h5>体能训练雷达图列表 </h5>
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
	<form:form id="searchForm" modelAttribute="gradePhysical" action="${ctx}/mycharts/GradePhysical2/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>学员：</span>
				<sys:gridselect url="${ctx}/mycharts/GradePhysical2/selecttrainee" id="trainee" name="trainee"  value="${gradePhysical.trainee.id}"  title="选择学员" labelName="trainee.name" 
					labelValue="${gradePhysical.trainee.name}" cssClass="form-control required" fieldLabels="学员" fieldKeys="name" searchLabel="学员" searchKey="name" ></sys:gridselect>
			<%-- <span>体能训练项：</span>
				<sys:gridselect url="${ctx}/mycharts/GradePhysical2/selecttrainPhysical" id="trainPhysical" name="trainPhysical"  value="${gradePhysical.trainPhysical.id}"  title="选择体能训练项" labelName="trainPhysical.name" 
					labelValue="${gradePhysical.trainPhysical.name}" cssClass="form-control required" fieldLabels="体能训练项" fieldKeys="name" searchLabel="体能训练项" searchKey="name" ></sys:gridselect> --%>
			<span>日期：</span>
				<input id="date" name="date" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${gradePhysical.date}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="mycharts:gradePhysical:add">
				<table:addRow url="${ctx}/mycharts/gradePhysical/form" title="体能训练雷达图"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradePhysical:edit">
			    <table:editRow url="${ctx}/mycharts/gradePhysical/form" title="体能训练雷达图" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradePhysical:del">
				<table:delRow url="${ctx}/mycharts/gradePhysical/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradePhysical:import">
				<table:importExcel url="${ctx}/mycharts/gradePhysical/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradePhysical:export">
	       		<table:exportExcel url="${ctx}/mycharts/gradePhysical/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column trainee.id">学员</th>
				<th  class="sort-column score">分数</th>
				<th  class="sort-column trainPhysical.id">体能训练项</th>
				<th  class="sort-column date">日期</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gradePhysical">
			<tr>
				<td> <input type="checkbox" id="${gradePhysical.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看体能训练雷达图', '${ctx}/mycharts/gradePhysical/form?id=${gradePhysical.id}','800px', '500px')">
					${gradePhysical.trainee.name}
				</a></td>
				<td>
					${gradePhysical.score}
				</td>
				<td>
					${gradePhysical.trainPhysical.name}
				</td>
				<td>
					<fmt:formatDate value="${gradePhysical.date}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${gradePhysical.remarks}
				</td>
				<td>
					<shiro:hasPermission name="mycharts:gradePhysical:view">
						<a href="#" onclick="openDialogView('查看体能训练雷达图', '${ctx}/mycharts/gradePhysical/form?id=${gradePhysical.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="mycharts:gradePhysical:edit">
    					<a href="#" onclick="openDialog('修改体能训练雷达图', '${ctx}/mycharts/gradePhysical/form?id=${gradePhysical.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="mycharts:gradePhysical:del">
						<a href="${ctx}/mycharts/gradePhysical/delete?id=${gradePhysical.id}" onclick="return confirmx('确认要删除该体能训练雷达图吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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