/*
 * @(#)AdminDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.util.List;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.domain.Faculty;

/**
 * Admin DAO interface, should be implemented by all Admin DAO objects.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public interface AdminDAO {

    /**
     * Must return all unapplied enrolles according to the faculty id.
     *
     * @param facultyId
     * @param entriesNumber
     * @return List<Enrollee> unapplied.
     */
    public List<Enrollee> getUnappliedEnrollees(int facultyId, int entriesNumber);

    /**
     * Must return all qualified enrolles according to the faculty id
     *
     * @param facultyId
     * @return List<Enrollee> qualified.
     */
    public List<Enrollee> getQualifiedEnrollees(int facultyId);

    /**
     * Must set qualified enrolles according to the faculty id
     *
     * @param qualifiedEnrollees
     * @return true if successful.
     */
    public boolean setQualificationEnrollee(List<Enrollee> qualifiedEnrollees);

    /**
     * Must change enrollee's state to 'applied'.
     *
     * @param enrolleeId
     * @return true if successful.
     */
    public boolean applyEnrollee(int enrolleeId);

    /**
     * Must change enrolee's state to 'cancelled'
     *
     * @param enroleeId
     * @return true if successful.
     */
    public boolean discardEnrollee(int enroleeId);

    /**
     * Must change the min required mark rate for the faculty.
     *
     * @param facultyId
     * @param rate
     * @return true if successful.
     */
    public boolean setMinRequiredRate(int facultyId, double rate);

    /**
     * Must return list of all existing faculties.
     *
     * @return List<Faculty> all faculties.
     */
    public List<Faculty> getAllFaculties();
}
