/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import core.Database;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class DeleteBook extends Dispatcher {

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
        //response.setContentType("text/html;charset=UTF-8");
        Connection conn = null;
        try {
            InitialContext initialContext = null;
            try {
                initialContext = new InitialContext();
            } catch (NamingException ex) {
                Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            }
            Context context = null;
            try {
                context = (Context) initialContext.lookup("java:comp/env");
            } catch (NamingException ex) {
                Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            }
            //The JDBC Data source that we just created
            DataSource ds = null;
            try {
                ds = (DataSource) context.lookup("jdbc/library");
            } catch (NamingException ex) {
                Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            }
            conn = ds.getConnection();
            
            String query = "DELETE FROM book WHERE id=" + request.getParameter("id");

            PreparedStatement statement = conn.prepareStatement(query);
            statement.executeUpdate();

            response.sendRedirect(request.getContextPath() + "/pages/books.jsp?success=true&genre_id=" + request.getParameter("genre_id"));

        } catch (SQLException ex) {
            Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect(request.getContextPath() + "/pages/books.jsp?success=false&genre_id=" + request.getParameter("genre_id"));
        } 
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException ex) {
//                    Logger.getLogger(DeleteBook.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
        
    }   
}

