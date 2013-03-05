/*
 * @(#)MySqlEnrolleeDAO.java      0.1 13/01/16
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
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.domain.Faculty;
import ua.epam.kalko.integration.util.MySqlGetFaculties;

/**
 * Concrete implementation for the MySQL DB.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
class MySqlEnrolleeDAO implements EnrolleeDAO {

    /**
     * Log4j logger for exceptions.
     */
    private final static Logger log = Logger.getLogger(MySqlEnrolleeDAO.class.getName());

    public MySqlEnrolleeDAO() {
    }

    /**
     * Returnes Faculty according to its id.
     *
     * @param facultyId
     * @return Faculty.
     */
    @Override
    public String getFaculty(int facultyId) {
        final String GET_QUERY = "SELECT faculty_title FROM faculties WHERE id = ?";
        Connection currentConnection = null;
        PreparedStatement getStatement;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            getStatement = currentConnection.prepareStatement(GET_QUERY);
            getStatement.setInt(1, facultyId);
            ResultSet res = getStatement.executeQuery();
            if (res.next()) {
                return res.getString(1);
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
                }
            }
        }
        return null;
    }

    /**
     * Returns Enrollee object WITHOUT Subjects in it.
     *
     * @param login
     * @return Enrollee (WITHOUT Subjects)
     */
    @Override
    public Enrollee getEnrolleeWithoutSubjects(String login) {
        final String GET_ENROLLEE_ID = "SELECT enrollee_id FROM authenification WHERE e_mail_login = ?";
        final String GET_ENROLLEE = "SELECT id, first_name, second_name, third_name, average_rate, "
                + "faculty_id, applied_by_admin, discarded_by_admin, rank FROM enrollees WHERE id = ?";
        int enrolleeId;
        Enrollee result;
        PreparedStatement getEnrolleeId;
        PreparedStatement getEnrollee;
        Connection currentConnection = null;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            getEnrolleeId = currentConnection.prepareStatement(GET_ENROLLEE_ID);
            getEnrollee = currentConnection.prepareStatement(GET_ENROLLEE);
            getEnrolleeId.setString(1, login);
            ResultSet res = getEnrolleeId.executeQuery();
            if (res.next()) {
                enrolleeId = res.getInt(1);
                getEnrollee.setInt(1, enrolleeId);
                ResultSet enrolleeSet = getEnrollee.executeQuery();
                if (enrolleeSet.next()) {
                    result = new Enrollee(enrolleeSet.getInt(1), enrolleeSet.getString(2),
                            enrolleeSet.getString(3), enrolleeSet.getString(4), enrolleeSet.getDouble(5),
                            enrolleeSet.getInt(6), null, enrolleeSet.getBoolean(7),
                            enrolleeSet.getBoolean(8), enrolleeSet.getInt(9));
                    return result;
                }
            }
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
