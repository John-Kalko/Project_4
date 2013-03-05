/*
 * @(#)GetEnrolleeResultCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.integration.EnrolleeDAO;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;

/**
 * ControllerCommand object. Sets information about enrollee as the session
 * attributes.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class EnrolleeGetResult implements ControllerCommand {

    /**
     * Constants, that represent session attributes
     */
    private final static String REDIRECT_PAGE = "enrollee_index.jspx";
    private final static String ERROR_PAGE = "global_error.jspx";
    
    private final static String ENROLLEE_INSTANCE = "enrollee_instance";
    private final static String USER_LOGIN_ATTR = "user_login";
    private final static String FACULTY_TITLE_ATTR = "faculty_title";
    private final static String NOT_QUALIFIED = "enrollee_not_qualified";
    private final static String QUALIFIED = "enrollee_qualified";
    private final static String QUALIFIED_RATE = "enrollee_qualified_rate";
    private final static String DISCARDED = "enrollee_discarded";
    private final static String NOT_CHECKED = "enrollee_not_checked";

    /**
     * Sets information about enrollee as the session attributes.
     *
     * @param request current request from the servlet.
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        /* Gets and initializes Enrollee object using DAO */
        HttpSession session = request.getSession(true);
        String login = session.getAttribute(USER_LOGIN_ATTR).toString();
        EnrolleeDAO dao = CommandsToMySqlDAOLinker.getInstance().getEnrolleeDAO(request);
        
        Enrollee currentEnrollee = dao.getEnrolleeWithoutSubjects(login);
        if (currentEnrollee != null) { /* If DAO fails */
            session.setAttribute(ENROLLEE_INSTANCE, currentEnrollee); /* Sends Enrollee object to the session */
            int facultyId = currentEnrollee.getFacultyId();
            String facultyTitle = dao.getFaculty(facultyId);
            if (facultyTitle != null) {
                session.setAttribute(FACULTY_TITLE_ATTR, facultyTitle); /* Sends Faculty's title to the session */
                setResult(currentEnrollee, session);
                return REDIRECT_PAGE;
            }
        }
        return ERROR_PAGE;
    }

    /**
     * Defines enrollee status (Applied, Not applied, Discarded, Passed) and his
     * rate.
     *
     * @param currentEnrollee Enrollee object to work with.
     * @param session current session.
     */
    private void setResult(Enrollee currentEnrollee, HttpSession session) {
        if (currentEnrollee.isApplied()) { /* If is applied */
            if (currentEnrollee.getRank() == 0) { /* If the rate is less than required */
                session.setAttribute(NOT_QUALIFIED, "true");
            } else {
                session.setAttribute(QUALIFIED, "true"); /* If the rate is OK */
                /* Sends rate to the session */
                session.setAttribute(QUALIFIED_RATE, currentEnrollee.getRank());
            }
        } else if (currentEnrollee.isCancelled()) { /* If cancelled */
            session.setAttribute(DISCARDED, "true");
        } else {
            session.setAttribute(NOT_CHECKED, "true"); /* If havn't checked yet */
        }
    }
}
