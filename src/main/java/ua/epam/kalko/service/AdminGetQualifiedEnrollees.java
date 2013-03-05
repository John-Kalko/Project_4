/*
 * @(#)GetQualifiedEnrolleesCommand.java      0.1 13/01/16
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
 * ControllerCommand object. Sets the list of qualified Enrollees as the session
 * attribute according to the faculty ID.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class AdminGetQualifiedEnrollees implements ControllerCommand {

    /**
     * Constants, that represent session attributes
     */
    private final String REDIRECTIVE_PAGE = "admin_apply_report.jspx";
    private final String FACULTY_ID = "faculty_id";
    private final String ENROLLES_LIST = "enrollees_final_list";

    /**
     * Sets the list of qualified Enrollees as the session attribute according
     * to the faculty ID.
     *
     * @param request current request from the servlet.
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        int facultyId = Integer.parseInt(request.getParameter(this.FACULTY_ID));
        AdminDAO dao = CommandsToMySqlDAOLinker.getInstance().getAdminDAO(request);

        List<Enrollee> source = dao.getQualifiedEnrollees(facultyId);
        ArrayList<Enrollee> resultList = new ArrayList<Enrollee>(source);

        if (!resultList.isEmpty()) {
            request.getSession().setAttribute(this.ENROLLES_LIST, source);
            request.getSession().setAttribute(this.FACULTY_ID, facultyId);
        }
        return this.REDIRECTIVE_PAGE;
    }
}
