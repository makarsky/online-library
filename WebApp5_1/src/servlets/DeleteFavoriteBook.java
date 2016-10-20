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
public class DeleteFavoriteBook extends Dispatcher {

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
        request.setCharacterEncoding("UTF-8");
        ServletContext ctx = getServletContext();
        HttpSession session = request.getSession();
        
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

            HashMap userMap = (HashMap) ctx.getAttribute("userMap");
            User user = (User) userMap.get(session.getId());

            String query = "DELETE FROM favorites WHERE book_id=" + request.getParameter("id")
                    + " AND user_id=" + user.getId();

            PreparedStatement statement = conn.prepareStatement(query);
            statement.executeUpdate();
            statement.close();
            
            user.deleteFromFavorites(Long.parseLong(request.getParameter("id")));

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
