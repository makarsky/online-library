package servlets;

import core.Messages;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Check extends Dispatcher {

    @Override
    public String getServletInfo() {
        return "Checking servlet";
    }

    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        ServletContext ctx = getServletContext();
        HttpSession session = request.getSession();
        
        if (request.getParameter("send") != null) {
            
            this.forward("/CheckUser", request, response);
            
        } else if (request.getParameter("exit") != null) {

            session.setAttribute("login", "");
            session.setAttribute("password", "");
            session.setAttribute("role", "");
            HashMap userMap = (HashMap)ctx.getAttribute("userMap");
            userMap.remove(session.getId());
            session.invalidate();
            
            this.forward("/", request, response);

        } else if (request.getParameter("previous") != null) {

            this.forward("/", request, response);

        } else if (request.getParameter("join") != null) {

            String lang = (String) session.getAttribute("lang");
            session.setAttribute("login", Messages.getMessage(lang, "guest"));
            response.sendRedirect(request.getContextPath() + "/pages/main.jsp");
            //this.forward("/pages/main.jsp", request, response);

        } else if (request.getParameter("register") != null) {
            this.forward("/Registration", request, response);
        }
    }
}
