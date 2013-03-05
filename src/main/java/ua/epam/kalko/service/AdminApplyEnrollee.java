/*
 * @(#)ApplyOrDiscardEnrolleeCommand.java      0.1 13/01/16
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
 * ControllerCommand object. Provides actions, using DAO, to apply or discard
 * enrollee.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class AdminApplyEnrollee implements ControllerCommand {

    /**
     * Constants, that represent redirect pages from this command
     */
    private final static String REDIRECT_PAGE = "admin_apply_enrollee.jspx";
    private final static String ERROR_PAGE = "global_error.jspx";
    /**
     * Constants, that represent session attributes
     */
    private final static String ENROLLEES_LIST_ATTR = "enrollees_list";
    private final static String IS_DONE_FLAG_ATTR = "flag_isDone";
    private final static String APPLY_VALUE = "apply";
    private final static String DISCARD_VALUE = "discard";

    /**
     * Provides actions, using DAO, to apply or discard enrollee.
     *
     * @param request
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {

        request.getSession().setAttribute("faculties_list",
                CommandsToMySqlDAOLinker.getInstance().getAdminDAO(request).getAllFaculties());


        Object source = request.getSession().getAttribute(ENROLLEES_LIST_ATTR);

        if (source == null) {
            return REDIRECT_PAGE;
        }

        if (source != null && source instanceof List) { /* If the attribute does not exists or its type is wrong */
            List<Enrollee> sourceList = (List<Enrollee>) source;
            ArrayList<Enrollee> enrollees = new ArrayList<Enrollee>(sourceList);
            if (makeActions(enrollees, request)) {
                request.getSession().setAttribute(ENROLLEES_LIST_ATTR, null);
                request.setAttribute(IS_DONE_FLAG_ATTR, "true"); /*Flag, which is used to show result table*/
                return REDIRECT_PAGE;
            }
        }
        return ERROR_PAGE;
    }

    /**
     * Checks the input from the *.jspx page and, if needed sets enrollee status
     * as 'applied' of 'discarded' using DAOFactory.
     *
     * @param enrollees
     * @param request
     * @return true if successful.
     */
    private boolean makeActions(ArrayList<Enrollee> enrollees, HttpServletRequest request) {
        boolean resultFlag = true;
        String action[];
        String valueOf;
        AdminDAO dao = CommandsToMySqlDAOLinker.getInstance().getAdminDAO(request);
        
        for (int i = 0; i < enrollees.size(); i++) {
            valueOf = String.valueOf(i + 1);
            action = request.getParameterValues(valueOf);
            for (int j = 0; j < action.length; j++) {
                if (action[j].equals(APPLY_VALUE)) {           /*If it was selected 'apply' on the *.jspx page*/
                    dao.applyEnrollee(enrollees.get(i).getId());
                } else if (action[j].equals(DISCARD_VALUE)) {  /*If it was selected 'discard' on the *.jspx page*/
                    dao.discardEnrollee(enrollees.get(i).getId());
                }
            }
        }
        return resultFlag;
    }
}
