package servlets;

import core.Messages;
import core.beans.User;
import javax.naming.Context;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;
import javax.naming.InitialContext;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.*;

public class CheckUser extends Dispatcher {

    @Override
    public String getServletInfo() {
        return "Checking user servlet";
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection connection = null;
        ServletContext ctx = getServletContext();
        HttpSession session = request.getSession();
        try {
            request.setCharacterEncoding("UTF-8");
            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            //The JDBC Data source that we just created
            DataSource ds = (DataSource) context.lookup("jdbc/library");
            connection = ds.getConnection();

            String query = "SELECT * FROM authorization";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            boolean success = false;

            while (rs.next()) {
                if (rs.getString("name").equals(request.getParameter("login"))
                        && rs.getString("password").equals(request.getParameter("password"))) {
//                    if (rs.getString("role").contains("USER")) {
                        success = true;
                        session.setAttribute("login", request.getParameter("login"));
                        session.setAttribute("password", request.getParameter("password"));
                        session.setAttribute("role", rs.getString("role"));

                        HashMap userMap = (HashMap) ctx.getAttribute("userMap");
                        User user = (User) userMap.get(session.getId());
                        user.setId(rs.getLong("id"));
                        user.setName(request.getParameter("login"));
                        user.setRole(request.getParameter("password"));
                        user.setRole(rs.getString("role"));

                        //response.sendRedirect(request.getContextPath() + "/pages/main.jsp");
                        //this.forward("/pages/main.jsp", request, response);

//                    } else if (rs.getString("role").contains("ADMIN")) {
//                        success = true;
//                        session.setAttribute("login", request.getParameter("login"));
//                        session.setAttribute("password", request.getParameter("password"));
//                        session.setAttribute("role", rs.getString("role"));
//
//                        HashMap userMap = (HashMap) ctx.getAttribute("userMap");
//                        User user = (User) userMap.get(session.getId());
//                        user.setId(rs.getLong("id"));
//                        user.setName(request.getParameter("login"));
//                        user.setRole(request.getParameter("password"));
//                        user.setRole(rs.getString("role"));

                        //response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp");
                        //this.forward("/pages/adminpanel.jsp", request, response);
//                    }
                }
            }

            // в случае несоответствия логина и пароля перенаправление на index.jsp
            if (!success) {
                request.setAttribute("errorMessage", Messages.getMessage(String.valueOf(session.getAttribute("lang")), "error.bad_login"));
                this.forward("/index.jsp", request, response);
            } else {

                HashMap userMap = (HashMap) ctx.getAttribute("userMap");
                User user = (User) userMap.get(session.getId());
                long user_id = user.getId();
                
                connection = ds.getConnection();

                query = "SELECT * FROM favorites where user_id=" + user_id;
                statement = connection.prepareStatement(query);
                rs = statement.executeQuery();

                //if (rs.first()) {
                    while (rs.next()) {
                        user.addToFavorites(rs.getLong("book_id"));
                    }
                //}

                if (user.getRole().contains("ADMIN")) {
                    response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/pages/main.jsp");
                }
            }
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CheckUser.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect(request.getContextPath() + "/index.jsp?errorMessage=error.db");
        } finally {

//            try {
//                if (connection != null) {
//                    connection.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(CheckUser.class.getName()).log(Level.SEVERE, null, ex);
//            }

        }
    }
}
