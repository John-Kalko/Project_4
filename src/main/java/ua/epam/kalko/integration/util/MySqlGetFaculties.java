/*
 * @(#)MySqlGuestDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import ua.epam.kalko.domain.Faculty;
import ua.epam.kalko.integration.ConnectorMySqlDAO;
import ua.epam.kalko.service.GuestGetFaculties;

/**
 * Utility object, which returns list of all existing Faculties.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class MySqlGetFaculties {

    /**
     * Log4j logger for exceptions.
     */
    private static Logger log = Logger.getLogger(GuestGetFaculties.class.getName());
    /**
     * Singleton.
     */
    private static MySqlGetFaculties instance = new MySqlGetFaculties();

    private MySqlGetFaculties() {
    }

    /**
     * Returns instance.
     *
     * @return MySqlGetFaculties.
     */
    public static MySqlGetFaculties getInstance() {
        if (instance == null) {
            instance = new MySqlGetFaculties();
        }
        return instance;
    }

    /**
     * Returns list of all existing Faculties.
     *
     * @return List<Faculty>
     */
    public List<Faculty> getFacultiesList() {
        final String GET_FACULTY = "SELECT * FROM faculties";
        LinkedList<Faculty> resultList = new LinkedList<Faculty>();
        int id;
        String faculty_title;
        String faculty_abbr;
        double passMark;
        int quota;
        Connection currentConnection = null;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();

            ResultSet res = currentConnection.createStatement().executeQuery(GET_FACULTY);
            while (res.next()) {
                id = res.getInt(1);
                faculty_title = res.getString(2);
                faculty_abbr = res.getString(3);
                passMark = res.getDouble(4);
                quota = res.getInt(5);
                resultList.add(new Faculty(id, faculty_title, faculty_abbr, passMark, quota));
            }
            return resultList;

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
                }
            }
        }
        return null;
    }
}
