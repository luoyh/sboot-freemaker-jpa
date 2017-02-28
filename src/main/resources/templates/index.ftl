<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	${timestamp}
	
	 <#list data as user>
	 <div>
	 	<span>${user.code}</span>---
	 	<span>${user.msg}</span>
	 </div>
    </#list>
</body>
</html>