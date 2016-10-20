/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import core.Database;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Lenovo
 */
@MultipartConfig(fileSizeThreshold = 1024*1024*2)
public class AddBook extends Dispatcher {

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

        request.setCharacterEncoding("UTF-8");

        if (request.getParameter("name").isEmpty()
                || request.getParameter("page_count").isEmpty()
                || request.getParameter("author").isEmpty()
                || request.getParameter("isbn").isEmpty()
                || request.getParameter("publish_year").isEmpty()
                || request.getParameter("publisher").isEmpty()
                || request.getPart("image") == null
                || request.getParameter("genre").isEmpty()
                || request.getPart("content") == null) {
            response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp?errorMessage=error.empty_field");

        } else {

            Part contentPart = request.getPart("content"); // Retrieves <input type="file" name="content">
            String contentName = Paths.get(contentPart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            
            Part imagePart = request.getPart("image");
            

            if (!contentName.endsWith(".pdf")) {
                response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp?errorMessage=error.not_pdf");
            } else {
                
                long max = 10500000L; 
                System.out.println(contentPart.getSize());
                
                if (contentPart.getSize() > max) {
                    response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp?errorMessage=error_big_weight");
                } else {
                
                InputStream content = contentPart.getInputStream();
                InputStream image = imagePart.getInputStream();

                try {
                    
                    connection = Database.getConnection();
                    String query = "SELECT name, isbn FROM book";

                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery();

                    boolean success = false;

                    while (rs.next()) {
                        if (rs.getString("name").equals(request.getParameter("name"))
                                || rs.getString("isbn").equals(request.getParameter("isbn"))) {
                            success = true;
                        }
                    }

                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp?errorMessage=error.unique");
                    } else {

                        query = "SELECT * FROM genre";

                        statement = connection.prepareStatement(query);
                        rs = statement.executeQuery();

                        int genre_id = 0;
                        while (rs.next()) {
                            if (rs.getString("name").equals(request.getParameter("genre"))) {
                                genre_id = rs.getInt("id");
                                break;
                            }
                        }

                        if (genre_id == 0) {
                            query = "INSERT INTO genre (name) VALUES (?)";

                            statement = connection.prepareStatement(query);
                            statement.setString(1, request.getParameter("genre"));
                            statement.executeUpdate();

                            query = "SELECT * FROM genre";

                            statement = connection.prepareStatement(query);
                            rs = statement.executeQuery();

                            while (rs.next()) {
                                if (rs.getString("name").equals(request.getParameter("genre"))) {
                                    genre_id = rs.getInt("id");
                                    break;
                                }
                            }
                        }

                        query = "SELECT * FROM author";

                        statement = connection.prepareStatement(query);
                        rs = statement.executeQuery();

                        int author_id = 0;
                        while (rs.next()) {
                            if (rs.getString("fio").equals(request.getParameter("author"))) {
                                author_id = rs.getInt("id");
                                break;
                            }
                        }
                        
                        if (author_id == 0) {
                            query = "INSERT INTO author (fio) VALUES (?)";

                            statement = connection.prepareStatement(query);
                            statement.setString(1, request.getParameter("author"));
                            statement.executeUpdate();

                            query = "SELECT * FROM author";

                            statement = connection.prepareStatement(query);
                            rs = statement.executeQuery();

                            while (rs.next()) {
                                if (rs.getString("fio").equals(request.getParameter("author"))) {
                                    author_id = rs.getInt("id");
                                    break;
                                }
                            }
                        }

//                        query = "SELECT * FROM publisher";
//
//                        statement = connection.prepareStatement(query);
//                        rs = statement.executeQuery();
//
//                        int publisher_id = 0;
//                        while (rs.next()) {
//                            if (rs.getString("name").equals(request.getParameter("publisher"))) {
//                                publisher_id = rs.getInt("id");
//                                break;
//                            }
//                        }
//                        
//                        if (publisher_id == 0) {
//                            connection = ds.getConnection();
//                            query = "INSERT INTO publisher (name) VALUES (?)";
//
//                            statement = connection.prepareStatement(query);
//                            statement.setString(1, request.getParameter("publisher"));
//                            statement.executeUpdate();
//
//                            connection = ds.getConnection();
//                            query = "SELECT * FROM publisher";
//
//                            statement = connection.prepareStatement(query);
//                            rs = statement.executeQuery();
//
//                            while (rs.next()) {
//                                if (rs.getString("name").equals(request.getParameter("publisher"))) {
//                                    publisher_id = rs.getInt("id");
//                                    break;
//                                }
//                            }
//                        }

                        query = "INSERT INTO book "
                                + "(name, content, page_count, isbn, genre_id, "
                                + "author_id, publish_year, publisher, image) "
                                + "VALUES (?,?,?,?,?,?,?,?,?);";

                        statement = connection.prepareStatement(query);
                        statement.setString(1, request.getParameter("name"));
                        statement.setBlob(2, content);
                        statement.setInt(3, Integer.valueOf(request.getParameter("page_count")));
                        statement.setString(4, request.getParameter("isbn"));
                        statement.setInt(5, genre_id);
                        statement.setInt(6, author_id);
                        statement.setInt(7, Integer.valueOf(request.getParameter("publish_year")));
                        statement.setString(8, request.getParameter("publisher"));
                        statement.setBlob(9, image);
                        statement.executeUpdate();

                        response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp?message=adminpanel.success");

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendRedirect(request.getContextPath() + "/pages/adminpanel.jsp?errorMessage=error.db");
                } finally {

//                    try {
//                        if (connection != null) {
//                            connection.close();
//                        }
//                    } catch (SQLException ex) {
//                        Logger.getLogger(AddBook.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
            }
            }
        }
    }
}
