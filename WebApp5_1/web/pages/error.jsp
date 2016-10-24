
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message bundle="${loc}" key="error"/></title>
    </head>
    <body>
        <h1 style="align-content: center"><fmt:message bundle="${loc}" key="error.db"/></h1>
        <a style="align-content: center" href="../index.jsp"><fmt:message bundle="${loc}" key="error.back"/></a>
    </body>
</html>
