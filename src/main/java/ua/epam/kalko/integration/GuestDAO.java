/*
 * @(#)GuestDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.util.List;
import ua.epam.kalko.domain.Faculty;

/**
 * Guest DAO interface, should be implemented by all Guest DAO objects.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public interface GuestDAO {

    /**
     * Stores enums with user categories.
     */
    public static enum UserLevel {

        ADMIN, USER
    };

    /**
     * Must return UserLevel according to user's login and password, if such
     * user exists.
     *
     * @param login
     * @param password
     * @return UserLevel
     */
    public UserLevel authenificate(String login, int password);

    /**
     * Must return list of all Faculties.
     *
     * @return List<Faculty>
     */
    public List<Faculty> getAllFaculties();
}
