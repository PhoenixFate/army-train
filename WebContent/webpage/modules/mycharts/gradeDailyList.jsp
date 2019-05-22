<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>日常训练统计图管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
<%@ include file="/webpage/include/echarts.jsp"%>
	<div class="wrapper wrapper-content">
	<!-- <div id="line_normal"  class="main000"></div> -->
	
    <div id="line_normal"  class="main000"></div>
    <echarts:line 
        id="line_normal"
		title="2011年温度对比曲线" 
		subtitle="主要城市的温度对比曲线"
		xAxisData="${xAxisData}" 
		yAxisData="${yAxisData}" 
		xAxisName="预测时间"
		yAxisName="温度(℃)" />
		
		
		
	<div class="ibox">
	<div class="ibox-title">
		<h5>日常训练统计图列表 </h5>
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
	<form:form id="searchForm" modelAttribute="gradeDaily" action="${ctx}/mycharts/gradeDaily/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>学员：</span>
				<sys:gridselect url="${ctx}/mycharts/gradeDaily/selecttrainee" id="trainee" name="trainee"  value="${gradeDaily.trainee.id}"  title="选择学员" labelName="trainee.name" 
					labelValue="${gradeDaily.trainee.name}" cssClass="form-control required" fieldLabels="学员" fieldKeys="name" searchLabel="学员" searchKey="name" ></sys:gridselect>
			<span>训练科目：</span>
				<sys:gridselect url="${ctx}/mycharts/gradeDaily/selecttrainItem" id="trainItem" name="trainItem"  value="${gradeDaily.trainItem.id}"  title="选择训练科目" labelName="trainItem.name" 
					labelValue="${gradeDaily.trainItem.name}" cssClass="form-control required" fieldLabels="训练科目" fieldKeys="name" searchLabel="训练科目" searchKey="name" ></sys:gridselect>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="mycharts:gradeDaily:add">
				<table:addRow url="${ctx}/mycharts/gradeDaily/form" title="日常训练统计图"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradeDaily:edit">
			    <table:editRow url="${ctx}/mycharts/gradeDaily/form" title="日常训练统计图" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradeDaily:del">
				<table:delRow url="${ctx}/mycharts/gradeDaily/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradeDaily:import">
				<table:importExcel url="${ctx}/mycharts/gradeDaily/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="mycharts:gradeDaily:export">
	       		<table:exportExcel url="${ctx}/mycharts/gradeDaily/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column trainItem.id">训练科目</th>
				<th  class="sort-column date">日期</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="gradeDaily">
			<tr>
				<td> <input type="checkbox" id="${gradeDaily.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看日常训练统计图', '${ctx}/mycharts/gradeDaily/form?id=${gradeDaily.id}','800px', '500px')">
					${gradeDaily.trainee.name}
				</a></td>
				<td>
					${gradeDaily.score}
				</td>
				<td>
					${gradeDaily.trainItem.name}
				</td>
				<td>
					<fmt:formatDate value="${gradeDaily.date}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${gradeDaily.remarks}
				</td>
				<td>
					<shiro:hasPermission name="mycharts:gradeDaily:view">
						<a href="#" onclick="openDialogView('查看日常训练统计图', '${ctx}/mycharts/gradeDaily/form?id=${gradeDaily.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="mycharts:gradeDaily:edit">
    					<a href="#" onclick="openDialog('修改日常训练统计图', '${ctx}/mycharts/gradeDaily/form?id=${gradeDaily.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="mycharts:gradeDaily:del">
						<a href="${ctx}/mycharts/gradeDaily/delete?id=${gradeDaily.id}" onclick="return confirmx('确认要删除该日常训练统计图吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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