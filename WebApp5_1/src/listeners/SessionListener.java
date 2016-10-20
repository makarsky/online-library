/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listeners;

import core.Messages;
import core.beans.User;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Web application lifecycle listener.
 *
 * @author Lenovo
 */
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();
        
        HashMap sessionMap = (HashMap) context.getAttribute("sessionMap");
        sessionMap.put(session.getId(), session);
        
        User user = new User(session.getId());
        String lang = (String) session.getAttribute("lang");
        
        user.setName(Messages.getMessage(lang, "guest"));
        user.setRole("GUEST");
        HashMap userMap = (HashMap) context.getAttribute("userMap");
        userMap.put(session.getId(), user);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        ServletContext context = session.getServletContext();
        HashMap sessionMap = (HashMap) context.getAttribute("sessionMap");
        sessionMap.remove(session.getId());
        
        HashMap userMap = (HashMap) context.getAttribute("userMap");
        userMap.remove(session.getId());
    }
}
