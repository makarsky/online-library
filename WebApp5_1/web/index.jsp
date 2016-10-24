<%@page import="core.Messages"%>
<%@page import="java.util.Locale"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

    <fmt:requestEncoding value="UTF-8"/>
    <c:if test="${not empty param.lang}">
        <fmt:setLocale value="${param.lang}" scope="application"/>
        <% session.setAttribute("lang", request.getParameter("lang"));%>
    </c:if>
    <fmt:setBundle basename="core.messages.msg" var="loc" scope="application"/>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            <c:if test="${not empty param.register}">
                <fmt:message bundle="${loc}" key="index.title"/>
            </c:if>
            <c:if test="${empty param.register}">
                <fmt:message bundle="${loc}" key="index.title"/>
            </c:if>

        </title>
        <link href="css/style_index.css" rel="stylesheet" type="text/css">
        <link rel="icon" href="http://www.freeiconspng.com/uploads/book-icon--icon-search-engine-6.png" type="image/x-icon">
        <link rel="shortcut icon" href="http://www.freeiconspng.com/uploads/book-icon--icon-search-engine-6.png" type="image/x-icon"> 
    </head>

    <body>
        
        <c:if test="${empty param.register}">

            <div class="main">

                <div class="content">
                    <a href="index.jsp?lang=en">en</a> 
                    <a href="index.jsp?lang=ru">ru</a> 
                    <a href="index.jsp?register=true" style="margin-left: 75%;">
                        <fmt:message bundle="${loc}" key="index.register"/>
                    </a>
                    <p class="title"><fmt:message bundle="${loc}" key="index.content_title"/></p>
                </div>

                <div class="login_div">
                    <p class="title"><fmt:message bundle="${loc}" key="index.login_div_title"/></p>
                    <form class="login_form" name="Authorisation" action="Check" method="POST">

                        <input type="submit" value="<fmt:message bundle="${loc}" key="index.join"/>" name="join" />
                        <p class="title"><fmt:message bundle="${loc}" key="index.login_div_title2"/>:</p>

                        <fmt:message bundle="${loc}" key="index.name"/>: 
                        <br>
                        <input type="text" name="login" value="<%= session.getAttribute("login") == null ? "" : session.getAttribute("login")%>" size="20" />
                        <br>
                        <fmt:message bundle="${loc}" key="index.password"/>: 
                        <br>
                        <input type="password" name="password" value="<%= session.getAttribute("password") == null ? "" : session.getAttribute("password")%>" size="20" />
                        <br>
                        <input type="submit" value="<fmt:message bundle="${loc}" key="index.send"/>" name="send" />
                        <!--input type="submit" value="<!fmt:message bundle="" key="index.register"/>" name="register" /-->

                        <c:if test="${not empty errorMessage}">
                            <p style="color: red"><c:out value="${errorMessage}"/></p>
                        </c:if>

                        <c:if test="${not empty param.errorMessage}">
                            <p style="color: red">
                                <%=Messages.getMessage(String.valueOf(session.getAttribute("lang")), request.getParameter("errorMessage"))%>
                            </p>
                        </c:if>

                        <c:if test="${not empty param.message}">
                            <p style="color: green">
                                <%=Messages.getMessage(String.valueOf(session.getAttribute("lang")), request.getParameter("message"))%>
                            </p>
                        </c:if>

                    </form>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty param.register}">

            <div class="main">
                <div class="content">
                    <a href="index.jsp?lang=en&register=true">en</a> 
                    <a href="index.jsp?lang=ru&register=true">ru</a> 
                    <a href="index.jsp" style="margin-left: 85%;">
                        <fmt:message bundle="${loc}" key="index.send"/>
                    </a>
                    <p class="title"><fmt:message bundle="${loc}" key="index.content_title"/></p>
                </div>

                <div class="login_div">
                    <p class="title"><fmt:message bundle="${loc}" key="fill_in_registration_form"/></p>
                    <form class="login_form" name="Registration" action="Check" method="POST">
                        <fmt:message bundle="${loc}" key="index.name"/>:
                        <br>
                        <input type="text" name="login" value="" size="20" required/>
                        <br>
                        <fmt:message bundle="${loc}" key="index.password"/>:
                        <br>
                        <input type="text" name="password" value="" size="20" required/>
                        <br>
                        <input type="submit" value="<fmt:message bundle="${loc}" key="index.register"/>" name="register" />

                        <c:if test="${not empty param.errorMessage}">
                            <p style="color: red">
                                <%=Messages.getMessage(String.valueOf(session.getAttribute("lang")), request.getParameter("errorMessage"))%>
                            </p>
                        </c:if>

                    </form>
                </div>

            </div>

        </c:if>

    </body>
</html>
