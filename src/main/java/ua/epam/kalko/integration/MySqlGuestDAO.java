/*
 * @(#)MySqlGuestDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;
import ua.epam.kalko.integration.util.MySqlGetFaculties;
import ua.epam.kalko.domain.Faculty;

/**
 * Concrete implementation for the MySQL DB.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen
 */
class MySqlGuestDAO implements GuestDAO {

    /**
     * Log4j logger for excceptions.
     */
    private final static Logger log = Logger.getLogger(MySqlGuestDAO.class.getName());

    public MySqlGuestDAO() {
    }

    /**
     * Returns UserLevel according to user's login and password, if such user
     * exists.
     *
     * @param login
     * @param password
     * @return UserLevel.
     */
    @Override
    public UserLevel authenificate(String login, int password) {
        final String GET_PASSWORD_QUERY = "SELECT password, level FROM "
                + "authenification WHERE e_mail_login = ?";
        Connection currentConnection = null;
        PreparedStatement getPasswordStatement;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            getPasswordStatement = currentConnection.prepareStatement(GET_PASSWORD_QUERY);
            getPasswordStatement.setString(1, login);
            ResultSet res = getPasswordStatement.executeQuery();
            res.next();
            int pass = res.getInt(1);
            String level = res.getString(2);

            if (pass == password) {
                if (level.equals(UserLevel.ADMIN.toString())) {
                    return UserLevel.ADMIN;
                }
                return UserLevel.USER;
            }
        } catch (SQLException ex) {
            log.error("DAO Exception. ", ex);
        } catch (Exception ex) {
            log.error("DAO Exception. ", ex);
        } finally {
            if (currentConnection != null) {
                try {
                    currentConnection.close();
                } catch (SQLException ex) {
                    log.error("DAO Exception. ", ex);
                } finally {
                    if (currentConnection != null) {
                        try {
                            currentConnection.close();
                        } catch (SQLException ex) {
                            log.error("DAO Exception. ", ex);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns list of all faculties.
     *
     * @return List<Faculties>
     */
    @Override
    public List<Faculty> getAllFaculties() {
        //Delegates to utilities object
        return MySqlGetFaculties.getInstance().getFacultiesList();
    }
}
