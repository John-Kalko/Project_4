/*
 * @(#)MySqlGuestDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.util.List;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.domain.Faculty;
import ua.epam.kalko.domain.Subject;

/**
 * Registration DAO interface, should be implemented by all Registration DAO
 * objects.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public interface RegistrationDAO {

    /**
     * Must return list of Subjects according to the Faculty id.
     *
     * @param facultyId
     * @return List<Subject>
     */
    public List<Subject> getSubjects(int facultyId);

    /**
     * Must add Enrollee to the DB.
     *
     * @param source
     * @param login
     * @param pass
     * @return true if successful.
     */
    public boolean addEnrollee(Enrollee source, String login, int pass);

    /**
     * Must search for the specified login.
     *
     * @param login
     * @return true if found
     */
    public boolean searchLogin(String login);

    /**
     * Must get list of all existing Faculties.
     *
     * @return List<Faculty>
     */
    public List<Faculty> getAllFaculties();
}
