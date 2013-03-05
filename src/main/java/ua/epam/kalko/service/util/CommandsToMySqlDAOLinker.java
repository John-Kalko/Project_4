/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.epam.kalko.service.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ua.epam.kalko.integration.AdminDAO;
import ua.epam.kalko.integration.DAOFactory;
import ua.epam.kalko.integration.EnrolleeDAO;
import ua.epam.kalko.integration.GuestDAO;
import ua.epam.kalko.integration.RegistrationDAO;

/**
 *
 * Links users with different levels with appropriate DAO objects and stores
 * them as session attribute.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class CommandsToMySqlDAOLinker {

    //private static Logger log = Logger.getLogger("InfoLog");
    private static final String DAO_OBJECT_ATTR = "dao_attribute";
    private static CommandsToMySqlDAOLinker instance = new CommandsToMySqlDAOLinker();

    private CommandsToMySqlDAOLinker() {
    }

    /**
     * Singleton.
     *
     * @return CommandsToMySqlDAOLinker.
     */
    public static CommandsToMySqlDAOLinker getInstance() {
        if (instance == null) {
            instance = new CommandsToMySqlDAOLinker();
        }
        return instance;
    }

    /**
     * Links user with 'admin' level with appropriate DAO objects and stores it
     * as session attribute.
     *
     * @param src
     * @return AdminDAO.
     */
    public AdminDAO getAdminDAO(HttpServletRequest src) {
        HttpSession session = src.getSession();

        if (session.getAttribute(DAO_OBJECT_ATTR) != null
                && session.getAttribute(DAO_OBJECT_ATTR) instanceof AdminDAO) {
            //log.info("Admin DAO object has been returned for the session ID " + session.getId());
            return (AdminDAO) session.getAttribute(DAO_OBJECT_ATTR);
        } else {
            AdminDAO createdDAO = DAOFactory.getInstance(DAOFactory.MY_SQL).getAdmin();
            session.setAttribute(DAO_OBJECT_ATTR, createdDAO);
            //log.info("Admin DAO object has been created for the session ID " + session.getId());
            return createdDAO;
        }
    }

    /**
     * Links user with 'user' level with appropriate DAO objects and stores it
     * as session attribute.
     *
     * @param src
     * @return EnrolleeDAO
     */
    public EnrolleeDAO getEnrolleeDAO(HttpServletRequest src) {
        HttpSession session = src.getSession();

        if (session.getAttribute(DAO_OBJECT_ATTR) != null
                && session.getAttribute(DAO_OBJECT_ATTR) instanceof EnrolleeDAO) {
            //log.info("Enrollee DAO object has been returned for the session ID " + session.getId());
            return (EnrolleeDAO) session.getAttribute(DAO_OBJECT_ATTR);
        } else {
            EnrolleeDAO createdDAO = DAOFactory.getInstance(DAOFactory.MY_SQL).getEnrollee();
            session.setAttribute(DAO_OBJECT_ATTR, createdDAO);
            //log.info("Enrollee DAO object has been created for the session ID " + session.getId());
            return createdDAO;
        }
    }

    /**
     * Links user with 'guest' level with appropriate DAO objects and stores it
     * as session attribute.
     *
     * @param src
     * @return GuestDAO.
     */
    public GuestDAO getGusetDAO(HttpServletRequest src) {
        HttpSession session = src.getSession();

        if (session.getAttribute(DAO_OBJECT_ATTR) != null
                && session.getAttribute(DAO_OBJECT_ATTR) instanceof GuestDAO) {
            //log.info("Guest DAO object has been returned for the session ID " + session.getId());
            return (GuestDAO) session.getAttribute(DAO_OBJECT_ATTR);
        } else {
            GuestDAO createdDAO = DAOFactory.getInstance(DAOFactory.MY_SQL).getGuest();
            session.setAttribute(DAO_OBJECT_ATTR, createdDAO);
            //log.info("Guest DAO object has been created for the session ID " + session.getId());
            return createdDAO;
        }
    }

    /**
     * Links user with 'guest' level which attempting to make registration, with
     * appropriate DAO objects and stores it as session attribute.
     *
     * @param src
     * @return RegistrationDAO.
     */
    public RegistrationDAO getRegistrationDAO(HttpServletRequest src) {
        HttpSession session = src.getSession();

        if (session.getAttribute(DAO_OBJECT_ATTR) != null
                && session.getAttribute(DAO_OBJECT_ATTR) instanceof RegistrationDAO) {
            //log.info("Registration DAO object has been returned for the session ID " + session.getId());
            return (RegistrationDAO) session.getAttribute(DAO_OBJECT_ATTR);
        } else {
            RegistrationDAO createdDAO = DAOFactory.getInstance(DAOFactory.MY_SQL).getRegistration();
            session.setAttribute(DAO_OBJECT_ATTR, createdDAO);
            //log.info("Registration DAO object has been created for the session ID " + session.getId());
            return createdDAO;
        }
    }
}
