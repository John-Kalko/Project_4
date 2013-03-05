/*
 * @(#)NoCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * ControllerCommand object. Dummy command.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class GlobalNoCommand implements ControllerCommand {

    /**
     * Provides just redirection to the error page.
     *
     * @param request current request from the servlet.
     * @return error page adress.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        return "global_error.jspx";
    }
}
