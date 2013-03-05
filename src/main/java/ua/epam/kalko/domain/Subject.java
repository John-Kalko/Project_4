/*
 * @(#)Subject.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.domain;

/**
 * Entity object, which represents subject.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen
 */
public class Subject {

    /**
     * Subject id.
     */
    private int id;
    /**
     * Enrollee's subject mark.
     */
    private int subject_mark;

    /**
     * Main constructor.
     *
     * @param id
     * @param subject_mark
     */
    public Subject(int id, int subject_mark) {
        this.id = id;
        this.subject_mark = subject_mark;
    }

    /* Setters and getters */
    public int getSubject_mark() {
        return subject_mark;
    }

    public int getId() {
        return id;
    }
}
