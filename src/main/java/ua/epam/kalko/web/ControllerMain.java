/*
 * @(#)ControllerMain.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import ua.epam.kalko.service.ControllerCommand;

/**
 * Main controller. Provides information excange between model and view.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen
 */
public class ControllerMain extends HttpServlet {

    private Logger log = null;

    /**
     * Initializes servlet and log4j object.
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        log = Logger.getLogger(ControllerMain.class.getName());
        String path = "WEB-INF\\log4j.xml";                 /*Path to log4j configuration file */
        String pref = getServletContext().getRealPath("/"); /*Path to the root path */
        DOMConfigurator.configure(pref + path);             /*Log4j configurator */
    }

    /**
     * Processes requests for both HTTP. Links request with the respective
     * command.
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String result = null;
        try {
            ControllerCommand command = RequestToCommandLinker.getCommand(request.
                    getParameter("command"));           /*Returnes Command object by its name*/
            result = command.execute(request);/*Returnes *.jspx page name */
        } catch (ServletException ex) {
            log.error("Controller exception. ", ex);
        } catch (IOException ex) {
            log.error("Controller exception. ", ex);
        } catch (Exception ex) {
            log.error("Controller exception. ", ex);
        }
        request.getRequestDispatcher(result).forward(request, response);
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
