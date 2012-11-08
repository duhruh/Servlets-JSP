<%@ page import="java.util.Vector" import="java.io.PrintWriter" language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Remote Desktop Management</title>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
</head>
<body>
<h1 style="
    text-align: center;
">Welcome to the Remote Desktop Management System</h1>
<hr style="
    color: black;
">
<div id="middle" style="
    text-align: center;
">
<p>
Please enter any valid SQL query or update statement.<br>
If no query/update command is given the Execute button will display all suppliers information in the database.<br>
All execution results will appear below.
</p>

	<form method="POST" action='Controller' name="MySQL">
		<textarea id="textarea" name="textarea" rows="4" cols="50" style="height: 300px; width: 627px; margin-left: 0px; margin-right: 0px; "></textarea>
		<br/>
		<input type="submit" value="Execute" name="execute">
		<input type="reset" value="Clear" name="clear">
	</form>
</div>
<hr style="
    color: black;
">
<div id="footer" style="
    text-align: center;
">
<h3>Database Results</h3>
<%
	String results = "Run a Query";
	results = (String)request.getAttribute("results");
	PrintWriter mOut = response.getWriter();
	if(results == null){
		//System.out.println(results);
		results = "";
	}
%>
<%= results %>
</div>
</body>
</html>