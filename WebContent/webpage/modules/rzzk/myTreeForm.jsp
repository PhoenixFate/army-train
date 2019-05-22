<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>树测试管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body class="hideScroll">
	<form:form id="inputForm" modelAttribute="myTree" action="${ctx}/rzzk/myTree/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
			<tr>
				<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
				<td class="width-35">
					<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right">上级父级编号:</label></td>
				<td class="width-35">
					<sys:treeselect id="parent" name="parent.id" value="${myTree.parent.id}" labelName="parent.name" labelValue="${myTree.parent.name}"
						title="父级编号" url="/rzzk/myTree/treeData" extId="${myTree.id}" cssClass="form-control " allowClear="true"/>
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
				<td class="width-35">
					<form:input path="name" htmlEscape="false"    class="form-control required"/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right">排序：</label></td>
				<td class="width-35">
					<form:input path="sort" htmlEscape="false"    class="form-control "/>
				
				</td>
			</tr>
			<tr>
				<td class="width-15 active"><label class="pull-right">编号：</label></td>
				<td class="width-35">
					<form:input path="code" htmlEscape="false"    class="form-control "/>
				
				</td>
			</tr>
		</tbody>
	</table>
	</form:form>
</body>
</html>