/*
 * @(#)GetFacultiesListCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import ua.epam.kalko.domain.Faculty;
import ua.epam.kalko.integration.GuestDAO;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;

/**
 * ControllerCommand object. Sets the list of Faculties as the session
 * attributes.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class GuestGetFaculties implements ControllerCommand {

    /**
     * Logger object to make error logs.
     */
    private final static Logger log = Logger.getLogger(GuestGetFaculties.class.getName());
    /**
     * Constants, that represent session attributes
     */
    private final String REDIRECT_PAGE;
    private final static String DEFAULT_REDIRECT_PAGE = "global_faculties.jspx";
    private final static String FACULTIES_LIST_ATTR = "faculties_list";
    private final static String ERROR_PAGE = "global_error.jspx";

    public GuestGetFaculties() {
        REDIRECT_PAGE = DEFAULT_REDIRECT_PAGE;
    }

    public GuestGetFaculties(String redirectPage) {
        REDIRECT_PAGE = redirectPage;
    }

    /**
     * Sets the list of Faculties as the session attributes.
     *
     * @param request current request from the servlet.
     * @return value, stored in the request parameter: 'target_page'.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        
        List<Faculty> fList = getFacListFromDao(request);
        if(fList != null) {
            LinkedList<Faculty> faculties = new LinkedList<Faculty>(fList);
            request.getSession().setAttribute(FACULTIES_LIST_ATTR, faculties);
            return REDIRECT_PAGE;
        } else {
            log.error("Cannot get list of faculties from the DAO");
            return ERROR_PAGE;
        }
    }

    /**
     * Sets the list of faculties as the session attribute.
     *
     * @param request
     * @return
     */
    protected List<Faculty> getFacListFromDao(HttpServletRequest request) {
        GuestDAO dao = CommandsToMySqlDAOLinker.getInstance().getGusetDAO(request);
        
        List<Faculty> source = dao.getAllFaculties();
        return source;
    }
}
