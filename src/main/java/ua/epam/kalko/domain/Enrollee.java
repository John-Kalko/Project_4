/*
 * @(#)Enrollee.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.domain;

import java.util.LinkedList;

/**
 * Entity object, which represents Enrollee.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen
 */
public class Enrollee {

    /**
     * Id, auto-generated bt DB.
     */
    private int id;
    /**
     * Enrollee's first name.
     */
    private String firstName;
    /**
     * Enrollee's last name.
     */
    private String lastName;
    /**
     * Enrollee's patronymic.
     */
    private String thirdName;
    /**
     * Enrollee's average rate.
     */
    private double averageRate;
    /**
     * Enrollee's faculty id.
     */
    private int facultyId;
    /**
     * If applied by admin.
     */
    private boolean applied;
    /**
     * If cancelled by admin.
     */
    private boolean cancelled;
    /**
     * Enrollee's subjects.
     */
    private LinkedList<Subject> subjects;
    /**
     * Enrollee's current place in the faculty list.
     */
    int rank;

    /**
     * Main constructor.
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param thirdName
     * @param averageRate
     * @param facultyId
     * @param subjects
     * @param applied
     * @param cancelled
     * @param rank
     */
    public Enrollee(int id, String firstName, String lastName, String thirdName,
            double averageRate, int facultyId, LinkedList<Subject> subjects,
            boolean applied, boolean cancelled, int rank) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thirdName = thirdName;
        this.averageRate = averageRate;
        this.facultyId = facultyId;
        this.applied = applied;
        this.cancelled = cancelled;
        this.subjects = subjects;
        this.rank = rank;
    }

    /**
     * Constructor for adding new object to DB.
     *
     * @param firstName
     * @param lastName
     * @param thirdName
     * @param averageRate
     * @param facultyId
     * @param subjects
     */
    public Enrollee(String firstName, String lastName, String thirdName, double averageRate,
            int facultyId, LinkedList<Subject> subjects) {
        this(0, firstName, lastName, thirdName, averageRate, facultyId, subjects, false, false, 0);
    }

    /**
     * Constructor (requires 'id' to be setted).
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param thirdName
     * @param averageRate
     * @param facultyId
     * @param subjects
     */
    public Enrollee(int id, String firstName, String lastName, String thirdName,
            double averageRate, int facultyId, LinkedList<Subject> subjects) {
        this(id, firstName, lastName, thirdName, averageRate, facultyId, subjects, false, false, 0);
    }

    /* Setters and getters */
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public double getAverageRate() {
        return averageRate;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public boolean isApplied() {
        return applied;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public LinkedList<Subject> getSubjects() {
        return subjects;
    }

    public int getRank() {
        return rank;
    }
}
