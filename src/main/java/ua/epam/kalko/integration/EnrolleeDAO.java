/*
 * @(#)EnrolleeDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.util.List;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.domain.Faculty;

/**
 * Enrollee DAO interface, should be implemented by all Enrollee DAO objects.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public interface EnrolleeDAO {

    /**
     * Must return Faculty object by its id.
     *
     * @param facultyId
     * @return Faculty
     */
    public String getFaculty(int facultyId);

    /**
     * Must return Enrollee object without list of Subjects.
     *
     * @param login
     * @return Enrollee
     */
    public Enrollee getEnrolleeWithoutSubjects(String login);

    /**
     * Must return list of all faculties.
     *
     * @return List<Faculty>
     */
    public List<Faculty> getAllFaculties();
}
