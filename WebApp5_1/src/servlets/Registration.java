/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Lenovo
 */
public class Registration extends Dispatcher {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        Connection connection = null;
        try {
            request.setCharacterEncoding("UTF-8");

            if (request.getParameter("login").isEmpty() || request.getParameter("password").isEmpty()) {

                response.sendRedirect(request.getContextPath() + "/index.jsp?register=true&errorMessage=error.empty_fields");
                //this.forward("/index.jsp?register=true&errorMessage=error.empty_fields", request, response);
            } else if (request.getParameter("login").length() < 6 || request.getParameter("password").length() < 6) {

                response.sendRedirect(request.getContextPath() + "/index.jsp?register=true&errorMessage=error.length");
                //this.forward("/index.jsp?register=true&errorMessage=error.length", request, response);
            } else {

                InitialContext initialContext = new InitialContext();
                Context context = (Context) initialContext.lookup("java:comp/env");
                //The JDBC Data source that we just created
                DataSource ds = (DataSource) context.lookup("jdbc/library");
                connection = ds.getConnection();

                String query = "SELECT name FROM authorization";

                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery();

                boolean nameAlreadyExists = false;

                while (rs.next()) {
                    if (rs.getString("name").equals(request.getParameter("login"))) {

                        response.sendRedirect(request.getContextPath() + "/index.jsp?register=true&errorMessage=error.user_already_exists");
                        nameAlreadyExists = true;
                        //this.forward("/index.jsp?register=true&errorMessage=error.user_already_exists", request, response);

                    }
                }

                if (!nameAlreadyExists) {

                    connection = ds.getConnection();
                    query = "INSERT INTO authorization (name, password, role) VALUES (?, ?, ?)";

                    statement = connection.prepareStatement(query);
                    statement.setString(1, request.getParameter("login"));
                    statement.setString(2, request.getParameter("password"));
                    statement.setString(3, "USER");
                    statement.executeUpdate();

//            HttpSession session = request.getSession();
//            session.setAttribute("login", request.getParameter("login"));
//            session.setAttribute("password", request.getParameter("password"));
//            session.setAttribute("role", "USER");
                    response.sendRedirect(request.getContextPath() + "/index.jsp?message=registration_successful");
                    //this.forward("/index.jsp?message=registration_successful", request, response);
                }
            }

        } catch (NamingException | SQLException ex) {
            Logger.getLogger(CheckUser.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect(request.getContextPath() + "/index.jsp?errorMessage=error.db");
        } finally {

            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CheckUser.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
