/*
 * @(#)ControllerCommandjava      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * All invocations of the execute() methods must be done through this interface.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public interface ControllerCommand {
    
    /**
     * Must be implemented.
     * @param request Current request from the servlet.
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException 
     */
    public String execute(HttpServletRequest request) throws ServletException, IOException; 
}
