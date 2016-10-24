
<%@page import="core.beans.Book"%>
<%@page import="java.util.ArrayList"%>
<%@include file="../WEB-INF/jspf/left_menu.jspf" %>
<%@include file="../WEB-INF/jspf/letters.jspf" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% 
    int id = Integer.valueOf(request.getParameter("index"));
    session.setAttribute("index", id);
    
%>

<iframe src="../pdf.js/web/viewer.html?file=<%=request.getContextPath()%>/PdfContent" width="750" height="500"></iframe>

