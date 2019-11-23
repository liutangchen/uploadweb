<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="${pageContext.request.contextPath }/uploadServlet" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td>请选择上传的第一个文件：</td>
				<td><input type="file" name="file1" /></td>
			</tr>
			<tr>
				<td>第一个上传的文件的描述信息：</td>
				<td><input type="text" name="desc1" /></td>
			</tr>
			<tr>
				<td><input type="submit" value="点击上传文件" /></td>
				<td><button id="addFile">点击新增加一个附件</button></td>
			</tr>
		</table>
	</form>
</body>
</html>