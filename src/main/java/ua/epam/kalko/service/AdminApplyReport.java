/*
 * @(#)ApplyReportCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.integration.AdminDAO;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;

/**
 * ControllerCommand object. Provides actions, using DAO, to apply the faculty
 * report.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class AdminApplyReport implements ControllerCommand {

    /* Logger object to make information logs */
    private final static Logger infoLog = Logger.getLogger("InfoLog");
    /* Constants, that represent redirect pages from this command */
    private final static String REDIRECT_PAGE = "admin_apply_report.jspx";
    private final static String ERROR_PAGE = "global_error.jspx";
    /**
     * Constants, that represent session attributes
     */
    private final static String ENROLLEES_LIST_ATTR = "enrollees_final_list";
    private final static String IS_DONE_FLAG_ATTR = "flag_isDone";
    private final static String FACULTY_ID_ATTR = "faculty_id";
    private final static String ERROR_ATTR = "flag_isErrorOccured";

    /**
     * Provides actions, using DAO, to apply the faculty report.
     *
     * @param request
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        Object source = request.getSession().getAttribute(ENROLLEES_LIST_ATTR);
        AdminDAO dao = CommandsToMySqlDAOLinker.getInstance().getAdminDAO(request);
        request.getSession().setAttribute("faculties_list",
                CommandsToMySqlDAOLinker.getInstance().getAdminDAO(request).getAllFaculties());
        
        if (source == null) {
            return REDIRECT_PAGE;
        }
        
        if (source != null && source instanceof List) { /* If the attribute does not exists or its type is wrong */
            List<Enrollee> sourceList = (List<Enrollee>) source;
            
            /* Applies enrollees list for the faculty using DAO */
            if (dao.setQualificationEnrollee(sourceList)) {
                request.getSession(true).setAttribute(ENROLLEES_LIST_ATTR, null); /* Refreshes enrollees list */
                request.setAttribute(IS_DONE_FLAG_ATTR, "true"); /* Used to show resulted table on the *.jspx page */
                infoLog.info("!!! Report for faculty ID: " + request.getSession(true).
                        getAttribute(FACULTY_ID_ATTR) + " was succesfully formed !!!");
                return REDIRECT_PAGE;
            } else {
                request.setAttribute(ERROR_ATTR, "true");
                return REDIRECT_PAGE;
            }
        }
        return ERROR_PAGE;
    }
}
