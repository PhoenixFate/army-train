<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>日常训练统计图管理</title>
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
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
					laydate({
			            elem: '#date', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="gradeDaily" action="${ctx}/mycharts/gradeDaily/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>学员：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/mycharts/gradeDaily/selecttrainee" id="trainee" name="trainee.id"  value="${gradeDaily.trainee.id}"  title="选择学员" labelName="trainee.name" 
						 labelValue="${gradeDaily.trainee.name}" cssClass="form-control required" fieldLabels="学员" fieldKeys="name" searchLabel="学员" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分数：</label></td>
					<td class="width-35">
						<form:input path="score" htmlEscape="false"    class="form-control required number"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>训练科目：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/mycharts/gradeDaily/selecttrainItem" id="trainItem" name="trainItem.id"  value="${gradeDaily.trainItem.id}"  title="选择训练科目" labelName="trainItem.name" 
						 labelValue="${gradeDaily.trainItem.name}" cssClass="form-control required" fieldLabels="训练科目" fieldKeys="name" searchLabel="训练科目" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>日期：</label></td>
					<td class="width-35">
						<input id="date" name="date" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${gradeDaily.date}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>