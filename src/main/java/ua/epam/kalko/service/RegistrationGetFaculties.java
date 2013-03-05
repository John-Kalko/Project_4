/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.epam.kalko.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;
import ua.epam.kalko.integration.RegistrationDAO;
import ua.epam.kalko.domain.Faculty;

/**
 * ControllerCommand object. Returns list of Faculties.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class RegistrationGetFaculties extends GuestGetFaculties {

    private final static String REDIRECT_PAGE = "registration_page_1.jspx";
    
    public RegistrationGetFaculties() {
        super(REDIRECT_PAGE);
    }

        /**
     * Returns list of Faculties.
     *
     * @param request
     * @return List<Faculty>
     */
    @Override
    protected List<Faculty> getFacListFromDao(HttpServletRequest request) {
        RegistrationDAO dao = CommandsToMySqlDAOLinker.getInstance().getRegistrationDAO(request);
        
        List<Faculty> source = dao.getAllFaculties();
        return source;
    }
    
}
