/*
 * @(#)MySqlDAOFactory.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

/**
 * DAO Factory for the MySQL database.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class MySqlDAOFactory extends DAOFactory {

    /**
     * Creates an object to execute commands from authenticated users.
     *
     * @return
     */
    @Override
    public EnrolleeDAO getEnrollee() {
        return new MySqlEnrolleeDAO();
    }

    /**
     * Creates an object to execute commands from non-authenticated users.
     *
     * @return
     */
    @Override
    public GuestDAO getGuest() {
        return new MySqlGuestDAO();
    }
    
    /**
     * Creates an object to execute commands from authenticated admin.
     *
     * @return
     */
    @Override
    public AdminDAO getAdmin() {
        return new MySqlAdminDAO();
    }

    /**
     * Creates an object to execute commands for regisrtation.
     *
     * @return
     */
    @Override
    public RegistrationDAO getRegistration() {
        return new MySqlRegistrationDAO();
    }
}
