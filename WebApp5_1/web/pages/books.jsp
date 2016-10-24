
<%@page import="core.beans.User"%>
<%@page import="java.util.HashMap"%>
<%@page import="core.enums.SearchType"%>
<%@page import="core.beans.Book"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@include file="../WEB-INF/jspf/left_menu.jspf" %>

<jsp:useBean id="booklist" scope="page" class="core.beans.BookList" />

<%@include file="../WEB-INF/jspf/letters.jspf" %>

<%    request.setCharacterEncoding("UTF-8");
%>

<c:set var="admin" value="${sessionScope.role eq 'ADMIN'}"/>


<div class="book_list">
    <c:if test="${param.success eq 'true'}">
        <h3 style="color: green"><fmt:message bundle="${loc}" key="books.success_true"/></h3>
    </c:if>
    <c:if test="${param.success eq 'false'}">
        <h3 style="color: red"><fmt:message bundle="${loc}" key="books.success_false"/></h3>
    </c:if>

    <%
        ArrayList<Book> list = null;
        HashMap userMap = (HashMap) getServletContext().getAttribute("userMap");
        User user = (User) userMap.get(session.getId());

        if (request.getParameter("genre_id") != null) {
            long genreId = Long.valueOf(request.getParameter("genre_id"));
            list = booklist.getBooksByGenre(genreId);
        } else if (request.getParameter("letter") != null) {
            String letter = request.getParameter("letter");
            session.setAttribute("letter", letter);
            list = booklist.getBooksByLetter(letter);
        } else if (request.getParameter("favorites") != null) {
            //String favorites = request.getParameter("favorites");
            //session.setAttribute("favorites", favorites);

            if (user.getFavorites().size() != 0) {
                list = booklist.getFavoriteBooks(user.getFavorites());
            }
        } else if (request.getParameter("currentBookList") != null) {
            list = (ArrayList<Book>) session.getAttribute("currentBookList");
        } else if (request.getParameter("search_string") != null) {
            String searchStr = request.getParameter("search_string");
            SearchType type = SearchType.TITLE;

            if (request.getParameter("search_option").equals("Фамилия")
                    || request.getParameter("search_option").equals("Last Name")) {
                type = SearchType.AUTHOR;
            }

            if (!searchStr.isEmpty() && !searchStr.trim().equals("")) {
                list = booklist.getBooksBySearch(searchStr, type);
            }

        }
        if (list != null) {
    %>
    <h5 style="text-align: left; margin-top:20px;"><fmt:message bundle="${loc}" key="books.found"/>: <%= list.size()%></h5>
    <%
        boolean success = false;
        session.setAttribute("currentBookList", list);

        for (Book book : list) {
    %>

    <div class="book_info">
        <div class="book_title">
            <p><a href="view.jsp?index=<%=book.getId()%>"><%=book.getName()%></a></p>
        </div>
        <div class="book_image">
            <a href="view.jsp?index=<%=book.getId()%>"><img src="<%=request.getContextPath()%>/ShowImage?index=<%=list.indexOf(book)%>" height="250" width="190" alt="Обложка"/></a>
        </div>
        <div class="book_details">
            <br><strong>ISBN:</strong> <%=book.getIsbn()%>
            <br><strong><fmt:message bundle="${loc}" key="adminpanel.publisher"/>:</strong> <%=book.getPublisher()%>
            <br><strong><fmt:message bundle="${loc}" key="adminpanel.page_count"/>:</strong> <%=book.getPageCount()%>
            <br><strong><fmt:message bundle="${loc}" key="adminpanel.publish_year"/>:</strong> <%=book.getPublishDate()%>
            <br><strong><fmt:message bundle="${loc}" key="adminpanel.author"/>:</strong> <%=book.getAuthor()%>
            <p style="margin:10px;">
                <a href="view.jsp?index=<%=book.getId()%>"><fmt:message bundle="${loc}" key="books.read"/></a>

                <c:if test="${sessionScope.role eq 'USER'}">
                    <%
                        success = false;
                        if (user.getFavorites() != null) {
                            for (long book_id : user.getFavorites()) {
                                if (book_id == book.getId()) {
                    %>
                    <br>
                    <a href="../DeleteFavoriteBook?id=<%= book.getId()%>">
                        <fmt:message bundle="${loc}" key="delete_from_favorites"/></a>
                    <%
                                    success = true;
                                    break;
                                }
                            }
                        }
                        if (!success) {
                    %>
                    <br>
                    <a href="../AddFavoriteBook?id=<%= book.getId()%>">
                        <fmt:message bundle="${loc}" key="add_to_favorites"/></a>
                    <%
                        }%>
                </c:if>

                <c:if test="${sessionScope.role eq 'ADMIN'}">

                    <a onclick="return confirm('<fmt:message bundle="${loc}" key="adminpanel.are_you_sure"/> <%= book.getName()%>?') ? true : false" href="../DeleteBook?id=<%= book.getId()%>&genre_id=${param.genre_id}">
                        <fmt:message bundle="${loc}" key="books.delete"/></a>

                    <%
                        success = false;
                        if (user.getFavorites() != null) {
                            for (long book_id : user.getFavorites()) {
                                if (book_id == book.getId()) {
                    %>
                    <br>
                    <a href="../DeleteFavoriteBook?id=<%= book.getId()%>">
                        <fmt:message bundle="${loc}" key="delete_from_favorites"/></a>
                    <%
                                    success = true;
                                    break;
                                }
                            }
                        }
                        if (!success) {
                    %>
                    <br>
                    <a href="../AddFavoriteBook?id=<%= book.getId()%>">
                        <fmt:message bundle="${loc}" key="add_to_favorites"/></a>
                    <%
                        }%>
                </c:if>

            </p>
        </div>
    </div>

    <% }
    } else { %>
    <h5 style="text-align: left; margin-top:20px;"><fmt:message bundle="${loc}" key="books.found"/>: 0</h5>
    <% }%>
</div>