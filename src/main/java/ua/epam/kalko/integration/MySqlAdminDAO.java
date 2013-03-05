/*
 * @(#)MySqlAdminDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
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
 * @author Kalko Ievgen.
 */
class MySqlAdminDAO implements AdminDAO {

    /**
     * Log4j logger for exceptions.
     */
    private final static Logger log = Logger.getLogger(MySqlAdminDAO.class.getName());

    public MySqlAdminDAO() {
    }

    /**
     * Changes enrollee's state to accepted in DB.
     *
     * @param id enrollee id.
     * @return true if successful.
     */
    @Override
    public boolean applyEnrollee(int id) {
        final String SET_QUERY = "UPDATE enrollees SET applied_by_admin = 1 WHERE id = ?";
        Connection currentConnection = null;
        PreparedStatement setStatement;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            setStatement = currentConnection.prepareStatement(SET_QUERY);
            setStatement.setInt(1, id);
            int result = setStatement.executeUpdate();

            if (result == 1) {
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
     * Returnes unapplied enrollees according to the faculty id.
     *
     * @param facultyId
     * @param entriesNumber
     * @return List<Enrollee> unapplied.
     */
    @Override
    public List<Enrollee> getUnappliedEnrollees(int facultyId, int entriesNumber) {
        final String GET_ENROLLES_QUERY = "SELECT id, first_name, second_name, third_name, "
                + "average_rate FROM enrollees WHERE applied_by_admin = false AND faculty_id = ?"
                + " AND discarded_by_admin = 0";
        final String GET_SUBJECTS_RATES = "SELECT subject_id, subject_rate FROM enrollees_subjects"
                + " WHERE enrollee_id = ?";
        ArrayList<Enrollee> resultList = new ArrayList<Enrollee>(entriesNumber);
        LinkedList<Subject> subjectList;
        int enrolleeId;
        Connection currentConnection = null;
        PreparedStatement getEnrolles;
        PreparedStatement getSubjects;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            getEnrolles = currentConnection.prepareStatement(GET_ENROLLES_QUERY);
            getSubjects = currentConnection.prepareStatement(GET_SUBJECTS_RATES);
            getEnrolles.setInt(1, facultyId);
            ResultSet enrolleesSet = getEnrolles.executeQuery();
            ResultSet subjectsSet;
            for (int i = 0; i < entriesNumber && enrolleesSet.next(); i++) {
                subjectList = new LinkedList<Subject>();
                enrolleeId = enrolleesSet.getInt(1);
                getSubjects.setInt(1, enrolleeId);
                subjectsSet = getSubjects.executeQuery();
                while (subjectsSet.next()) {
                    subjectList.add(new Subject(subjectsSet.getInt(1), subjectsSet.getInt(2)));
                }
                resultList.add(new Enrollee(
                        enrolleeId, enrolleesSet.getString(2), enrolleesSet.getString(3),
                        enrolleesSet.getString(4), enrolleesSet.getDouble(5), facultyId, subjectList));
            }
            return resultList;
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
     * Changes enrollee's state to 'discarded' in DB.
     *
     * @param enroleeId
     * @return true if successful.
     */
    @Override
    public boolean discardEnrollee(int enroleeId) {
        final String SET_QUERY = "UPDATE enrollees SET discarded_by_admin = 1 WHERE id = ?";
        Connection currentConnection = null;
        PreparedStatement setStatement;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            setStatement = currentConnection.prepareStatement(SET_QUERY);
            setStatement.setInt(1, enroleeId);
            int result = setStatement.executeUpdate();
            if (result == 1) {
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
                }
            }
        }
        return false;
    }

    /**
     * Returns list of enrollees which have been accepted and won challenge.
     *
     * @param facultyId
     * @return List<Enrollee>
     */
    @Override
    public List<Enrollee> getQualifiedEnrollees(int facultyId) {
        final String GET_ENROLLES_QUERY = "SELECT id, first_name, second_name, third_name, "
                + "average_rate FROM enrollees WHERE applied_by_admin = true AND faculty_id = ?"
                + " AND discarded_by_admin = false ORDER BY average_rate DESC";
        final String GET_FACULTY_QUOTA = "SELECT quota FROM faculties WHERE id = ?";
        ArrayList<Enrollee> resultList;
        int facultyQuota = 0;
        double lowerRate = 0;
        Connection currentConnection = null;
        PreparedStatement getEnrolles;
        PreparedStatement getQuota;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            getEnrolles = currentConnection.prepareStatement(GET_ENROLLES_QUERY);
            getEnrolles.setInt(1, facultyId);
            getQuota = currentConnection.prepareStatement(GET_FACULTY_QUOTA);
            getQuota.setInt(1, facultyId);
            ResultSet quotaSet = getQuota.executeQuery();
            quotaSet.next();
            facultyQuota = quotaSet.getInt(1);
            if (facultyQuota != 0) {
                resultList = new ArrayList<Enrollee>(facultyQuota);
                ResultSet enrolleesSet = getEnrolles.executeQuery();
                for (int i = 0; i < facultyQuota && enrolleesSet.next(); i++) {
                    resultList.add(new Enrollee(
                            enrolleesSet.getInt(1), enrolleesSet.getString(2), enrolleesSet.getString(3),
                            enrolleesSet.getString(4), enrolleesSet.getDouble(5), facultyId, null));
                    if (i == (facultyQuota - 1)) {
                        lowerRate = enrolleesSet.getDouble(5);
                    }
                }
                if (enrolleesSet.next()) {
                    this.setMinRequiredRate(facultyId, lowerRate);
                }
                return resultList;
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
     * Sets list of enrollees which have been accepted and won challenge.
     *
     * @param qualifiedEnrollees
     * @return true if successful.
     */
    @Override
    public boolean setQualificationEnrollee(List<Enrollee> qualifiedEnrollees) {
        final String RESTORE_QUERY = "UPDATE enrollees SET rank = 0 WHERE faculty_id = ?"
                + " AND applied_by_admin = true";
        final String SET_QUERY = "UPDATE enrollees SET rank = ? WHERE id = ?";
        ArrayList<Enrollee> source = new ArrayList<Enrollee>(qualifiedEnrollees);
        int enrolleeId;
        if (!source.isEmpty()) {
            Connection currentConnection = null;
            PreparedStatement restoreStatement;
            PreparedStatement setStatement;
            try {
                currentConnection = ConnectorMySqlDAO.getConnection();
                setStatement = currentConnection.prepareStatement(SET_QUERY);
                restoreStatement = currentConnection.prepareStatement(RESTORE_QUERY);
                restoreStatement.setInt(1, source.get(0).getFacultyId());
                if (restoreStatement.executeUpdate() != 0) {
                    for (int i = 0; i < source.size(); i++) {
                        enrolleeId = source.get(i).getId();
                        setStatement.setInt(1, i + 1);
                        setStatement.setInt(2, enrolleeId);
                        setStatement.addBatch();
                    }
                    int[] checkResult = setStatement.executeBatch();
                    if (checkResult.length == source.size()) {
                        return true;
                    }
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
        }
        return false;
    }

    /**
     * Sets minimal enrollee's rate to win the challenge.
     *
     * @param facultyId
     * @param rate
     * @return true if successful.
     */
    @Override
    public boolean setMinRequiredRate(int facultyId, double rate) {
        final String SET_QUERY = "UPDATE faculties SET pass_mark = ? WHERE id = ?";
        Connection currentConnection = null;
        PreparedStatement setStatement;
        try {
            currentConnection = ConnectorMySqlDAO.getConnection();
            setStatement = currentConnection.prepareStatement(SET_QUERY);
            setStatement.setDouble(1, rate);
            setStatement.setInt(2, facultyId);
            int result = setStatement.executeUpdate();
            if (result == 1) {
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
                }
            }
        }
        return false;
    }

    /**
     * Returnes list of all faculties.
     *
     * @return List<Faculty> all
     */
    @Override
    public List<Faculty> getAllFaculties() {
        //Delegate to utility object.
        return MySqlGetFaculties.getInstance().getFacultiesList();
    }
}
