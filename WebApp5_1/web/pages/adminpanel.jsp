<%@page import="core.Messages"%>
<%@page import="java.io.File"%>
<%@include file="../WEB-INF/jspf/left_menu.jspf" %>
<%@include file="../WEB-INF/jspf/letters.jspf" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<c:if test="${not empty param.action}">
    <c:set var="selectedAction" scope="session" value="${param.action}"/>
</c:if>

<c:if test="${not empty param.id}">
    <c:set var="selectedId" scope="session" value="${param.id}"/>
</c:if>

<div class="book_list">

    <!--c:if test="{sessionScope.selectedAction eq 'addBook'}"-->

        <h3><fmt:message bundle="${loc}" key="adminpanel.add_book"/></h3>
        <br>
        <form action="../AddBook" method="POST" enctype="multipart/form-data">
            <table>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.title"/>:</td>
                    <td><input type="text" name="name" value="" required maxlength="35"/></td>
                </tr>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.page_count"/>:</td>
                    <td><input type="text" name="page_count" value="" required maxlength="4"/></td>
                </tr>
                <tr>
                    <td>ISBN:</td>
                    <td><input type="text" name="isbn" value="" required maxlength="25"/></td>
                </tr>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.publish_year"/>:</td>
                    <td><input type="text" name="publish_year" value="" required maxlength="4"/></td>
                </tr>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.author"/>:</td>
                    <td><input type="text" name="author" value="" required maxlength="25"/></td>
                </tr>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.publisher"/>:</td>
                    <td><input type="text" name="publisher" value="" required maxlength="25"/></td>
                </tr>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.genre"/>:</td>
                    <td><input type="text" name="genre" value="" required maxlength="25"/></td>
                </tr>



                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.pdf_book"/>:</td>
                    <td><input type="file" name="content" required accept="application/pdf"/></td>
                </tr>
                <tr>
                    <td><fmt:message bundle="${loc}" key="adminpanel.img_link"/>:</td>
                    <td><input type="file" name="image" required accept="image/jpeg"/></td>
                </tr>
            </table>
            <br>
            <input type="submit" name="add" value="<fmt:message bundle="${loc}" key="adminpanel.add"/>"/>

        </form>

    <!--/c:if-->

    <c:if test="${sessionScope.selectedAction eq 'deleteBook'}">
        <c:if test="${not empty param.title}">
            <fmt:message bundle="${loc}" key="adminpanel.are_you_sure"/> <b>${param.title}</b>?
            <a href="adminpanel.jsp?confirm=${sessionScope.selectedId}"><fmt:message bundle="${loc}" key="adminpanel.confirm"/></a>
        </c:if>

        <c:if test="${not empty param.confirm}">
            <sql:update dataSource="jdbc/library">
                DELETE FROM book
                WHERE id = ${param.confirm}
            </sql:update>
                <p style="color: green"><fmt:message bundle="${loc}" key="adminpanel.success"/></p>
        </c:if>
    </c:if>

    <c:if test="${not empty param.errorMessage}">
        <p style="color: red"><%=Messages.getMessage(request.getLocale().getLanguage(), request.getParameter("errorMessage"))%></p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p style="color: green"><%=Messages.getMessage(request.getLocale().getLanguage(), request.getParameter("message"))%></p>
    </c:if>

</div>
