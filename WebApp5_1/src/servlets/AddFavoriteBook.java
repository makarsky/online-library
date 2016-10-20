/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import core.beans.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 *
 * @author Lenovo
 */
public class AddFavoriteBook extends Dispatcher {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        ServletContext ctx = getServletContext();
        HttpSession session = request.getSession();
        
        Connection conn = null;
        InitialContext initialContext = null;
        Context context = null;
        DataSource ds = null;
        
        try {
            
            try {
                initialContext = new InitialContext();
            } catch (NamingException ex) {
                Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                context = (Context) initialContext.lookup("java:comp/env");
            } catch (NamingException ex) {
                Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            }
            //The JDBC Data source that we just created
            
            try {
                ds = (DataSource) context.lookup("jdbc/library");
            } catch (NamingException ex) {
                Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            }
            conn = ds.getConnection();
            
            HashMap userMap = (HashMap) ctx.getAttribute("userMap");
            User user = (User) userMap.get(session.getId());
            
            String query = "INSERT INTO favorites (user_id, book_id) VALUES (?, ?)";

            PreparedStatement statement = conn.prepareStatement(query);
            statement.setLong(1, user.getId());
            statement.setLong(2, Long.parseLong(request.getParameter("id")));
            statement.executeUpdate();
            statement.close();
            
            user.addToFavorites(Long.parseLong(request.getParameter("id")));
            response.sendRedirect(request.getContextPath() + "/pages/books.jsp?success=true&currentBookList=true");
            
        } catch (SQLException ex) {
            Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect(request.getContextPath() + "/pages/books.jsp?success=false&currentBookList=true");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
