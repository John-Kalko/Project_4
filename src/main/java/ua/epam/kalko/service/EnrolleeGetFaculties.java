/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.epam.kalko.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import ua.epam.kalko.domain.Faculty;
import ua.epam.kalko.integration.EnrolleeDAO;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;

/**
 * ControllerCommand object. Returns list of Faculties.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class EnrolleeGetFaculties extends GuestGetFaculties {

    /**
     * Returns list of Faculties.
     *
     * @param request
     * @return List<Faculty>
     */
    @Override
    protected List<Faculty> getFacListFromDao(HttpServletRequest request) {
        EnrolleeDAO dao = CommandsToMySqlDAOLinker.getInstance().getEnrolleeDAO(request);

        List<Faculty> source = dao.getAllFaculties();
        return source;
    }
}
