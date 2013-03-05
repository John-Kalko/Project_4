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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.domain.Faculty;
import ua.epam.kalko.domain.Subject;
import ua.epam.kalko.integration.util.MySqlGetFaculties;

/**
 * Concrete implementation for the MySQL DB.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen
 */
class MySqlRegistrationDAO implements RegistrationDAO {

    /**
     * Log4j logger for excceptions.
     */
    private final static Logger log = Logger.getLogger(MySqlGuestDAO.class.getName());

    public MySqlRegistrationDAO() {
    }

    /**
     * Searches for user with the specified login and returns true if such user
     * exists.
     *
     * @param login
     * @return true if exists.
     */
    @Override
    public boolean searchLogin(String login) {
        final String SEARCH_QUERY = "SELECT id FROM authenification WHERE e_mail_login = ?";
        Integer id = null;
        PreparedStatement searchForLogin;
        Connection currentConnection = null;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            searchForLogin = currentConnection.prepareStatement(SEARCH_QUERY);
            searchForLogin.setString(1, login);
            ResultSet res = searchForLogin.executeQuery();
            if (res.next()) {
                return true;
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
        return false;
    }

    /**
     * Returns list of Subjects according to the Faculty id.
     *
     * @param facultyId
     * @return List<Subject>
     */
    @Override
    public List<Subject> getSubjects(int facultyId) {
        ArrayList<Subject> result = new ArrayList<Subject>(4);
        int subjectId = 0;
        final String SEARCH_QUERY = "SELECT subject_faculty.subject_id FROM subjects, subject_faculty "
                + "WHERE subject_faculty.subject_id = subjects.id AND subject_faculty.faculty_id = ?";
        Connection currentConnection = null;
        PreparedStatement searchForSubjects;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            searchForSubjects = currentConnection.prepareStatement(SEARCH_QUERY);
            searchForSubjects.setInt(1, facultyId);
            ResultSet res = searchForSubjects.executeQuery();
            while (res.next()) {
                subjectId = res.getInt(1);
                if (subjectId != 0) {
                    result.add(new Subject(subjectId, 0));
                }
            }
            return result;

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
     * Adds enrollee to the DB.
     *
     * @param source
     * @param login
     * @param pass
     * @return true if successful.
     */
    @Override
    public boolean addEnrollee(Enrollee source, String login, int pass) {
        final String SET_ENROLLEE = "INSERT INTO enrollees (first_name,second_name,"
                + "third_name,average_rate,faculty_id) VALUES (?, ?, ?, ?, ?)";
        final String SET_SUBJECT = "INSERT INTO enrollees_subjects (enrollee_id, "
                + "subject_id, subject_rate) VALUES (?, ?, ?)";
        final String SET_AUTH = "INSERT INTO authenification (e_mail_login, password, "
                + "level, enrollee_id) VALUES (?, ?, ?, ?)";
        Connection currentConnection = null;
        PreparedStatement setEnrollee;
        PreparedStatement getEnrolleeId;
        PreparedStatement setSubject;
        PreparedStatement setAuth;
        int id = 0;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            currentConnection.setAutoCommit(false);
            setEnrollee = currentConnection.prepareStatement(SET_ENROLLEE, Statement.RETURN_GENERATED_KEYS);
            setEnrollee.setString(1, source.getFirstName());
            setEnrollee.setString(2, source.getLastName());
            setEnrollee.setString(3, source.getThirdName());
            setEnrollee.setDouble(4, source.getAverageRate());
            setEnrollee.setInt(5, source.getFacultyId());
            setEnrollee.executeUpdate();
            ResultSet idSet = setEnrollee.getGeneratedKeys();
            if (idSet != null && idSet.next()) {
                id = idSet.getInt(1);
            } else {
                currentConnection.rollback();
                return false;
            }

            Iterator<Subject> iterator = source.getSubjects().iterator();
            Subject currentSubject;
            setSubject = currentConnection.prepareStatement(SET_SUBJECT);
            while (iterator.hasNext()) {
                currentSubject = iterator.next();
                setSubject.setInt(1, id);
                setSubject.setInt(2, currentSubject.getId());
                setSubject.setInt(3, currentSubject.getSubject_mark());
                setSubject.addBatch();
            }
            setSubject.executeBatch();

            setAuth = currentConnection.prepareStatement(SET_AUTH);
            setAuth.setString(1, login);
            setAuth.setInt(2, pass);
            setAuth.setString(3, "USER");
            setAuth.setInt(4, id);
            int authResult = setAuth.executeUpdate();
            if (authResult != 1) {
                currentConnection.rollback();
                return false;
            }
            currentConnection.commit();
            return true;
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
        return false;
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
