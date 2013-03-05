/*
 * @(#)DAOFactory.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

/**
 * DAO Factory abstract class.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public abstract class DAOFactory {

    /**
     * Constant, which represents MySQL database.
     */
    public static final int MY_SQL = 1;

    /**
     * Must create EnrolleeDAO object.
     *
     * @return EnrolleeDAO object.
     */
    public abstract EnrolleeDAO getEnrollee();

    /**
     * Must create GuestDAO object.
     *
     * @return GuestDAO object.
     */
    public abstract GuestDAO getGuest();

    /**
     * Must create RegistrationDAO object.
     *
     * @return RegistrationDAO object.
     */
    public abstract RegistrationDAO getRegistration();
    
    /**
     * Must create AdminDAO object.
     *
     * @return AdminDAO object.
     */
    public abstract AdminDAO getAdmin();

    /**
     * Creates DAO for the database, specified in the parameter value.
     *
     * @param whichFactory represents database type.
     * @return DAO object.
     */
    public static DAOFactory getInstance(int whichFactory) {
        switch (whichFactory) {
            case MY_SQL:
                return new MySqlDAOFactory();
            default:
                return null;
        }
    }
}
