/*
 * @(#)GetEnrollesByFaculty.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.integration.AdminDAO;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;

/**
 * ControllerCommand object. Sets the list of Enrollees as the session
 * attributes according to the faculty ID.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class AdminGetEnrollees implements ControllerCommand {

    /**
     * Constants, that represent session attributes
     */
    private final static String REDIRECTIVE_PAGE = "admin_apply_enrollee.jspx";
    private final static String FACULTY_ID = "faculty_id";
    private final static String ENROLLEES_NUMBER = "enrollees_number";
    private final static String ENROLLES_LIST = "enrollees_list";

    /**
     * Sets the list of Enrollees as the session attributes according to the
     * faculty ID.
     *
     * @param request current request from the servlet.
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        int facultyId = Integer.parseInt(request.getParameter(FACULTY_ID));
        int numberOfEnrollesPerPage = Integer.parseInt(request.getParameter(ENROLLEES_NUMBER));
        
        AdminDAO dao = CommandsToMySqlDAOLinker.getInstance().getAdminDAO(request);
        
        List<Enrollee> source = dao.getUnappliedEnrollees(facultyId, numberOfEnrollesPerPage);
        ArrayList<Enrollee> resultList = new ArrayList<Enrollee>(source);
        
        if (!resultList.isEmpty()) { /* If DAO not fails */
            request.getSession(true).setAttribute(ENROLLES_LIST, source);
        }
        
        return REDIRECTIVE_PAGE;
    }
}
