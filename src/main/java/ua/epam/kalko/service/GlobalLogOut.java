/*
 * @(#)LogOutCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 * ControllerCommand object. Provides actions for the user invalidation from the
 * session.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class GlobalLogOut implements ControllerCommand {

    /**
     * Logger object to make information logs.
     */
    private final static Logger infoLog = Logger.getLogger("InfoLog");
    /**
     * Constant, which represents session attribute
     */
    private final String REDIRECTIVE_PAGE = "index.jspx";

    /**
     * Provides actions for the user invalidation from the session.
     *
     * @param request current request from the servlet.
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        infoLog.info("User with session ID: " + request.getSession().getId() + " logged out");
        request.getSession().invalidate();
        return this.REDIRECTIVE_PAGE;
    }
}
