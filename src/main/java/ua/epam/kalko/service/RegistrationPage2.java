/*
 * @(#)RegisterEnrolleeCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import ua.epam.kalko.domain.Enrollee;
import ua.epam.kalko.domain.Subject;
import ua.epam.kalko.integration.RegistrationDAO;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;

/**
 * ControllerCommand object. Checks the registration information inputted by
 * user and sets it as the session attributes.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class RegistrationPage2 implements ControllerCommand {

    /**
     * Stores constants with numbers, that are the same as in the database.
     */
    enum Subjects {

        UKRAINIAN_SUBJECT_ID(1),
        FOREIGN_SUBJECT_ID(2),
        MATHEMATICS_SUBJECT_ID(3),
        CHEMISTRY_SUBJECT_ID(4),
        PHYSICS_SUBJECT_ID(5),
        HISTORY_SUBJECT_ID(6);
        private int value;

        Subjects(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    };
    /**
     * Logger object to make information logs.
     */
    private final static Logger infoLog = Logger.getLogger("InfoLog");
    /**
     * Logger object to make error logs.
     */
    private final static Logger log = Logger.getLogger(RegistrationPage2.class.getName());
    /**
     * Constants for user input checking.
     */
    private final int MIN_GRADE_VALUE = 0;
    private final int MAX_GRADE_VALUE = 200;
    private final double MIN_VALUE_AVERAGE = 1.0;
    private final double MAX_VALUE_AVERAGE = 12.0;
    private final double MIN_VALUE_AVERAGE_200 = 106.0;
    private final double MAX_VALUE_AVERAGE_200 = 200.0;
    /**
     * Stores names of some page parameters.
     */
    private Properties pageParameters;
    /**
     * Stores estimates table for the average grades inputted by user.
     */
    private Properties conversionEstimates;
    /**
     * Path to the properties files.
     */
    private final static String COMMANDS_PROPERTIES_PATH = "../../../../Commands.properties";
    private final static String ESTIMATES_PROPERTIES_PATH = "../../../../ConversionEstimates12To200.properties";
    private HttpServletRequest request;
    /**
     * Stores the average score of the enrollee in the 200-ball format.
     */
    private double averageRate;
    /**
     * Stores the subjects of the enrollee.
     */
    private LinkedList<Subject> enrollee_subjects_rates;

    public RegistrationPage2() {
        this.pageParameters = new Properties();
        this.conversionEstimates = new Properties();

        try {
            this.pageParameters.load(getClass().getResourceAsStream(COMMANDS_PROPERTIES_PATH));
            this.conversionEstimates.load(getClass().getResourceAsStream(ESTIMATES_PROPERTIES_PATH));
        } catch (IOException ex) {
            log.error("Command error. Properties file loading fails", ex);
        }
    }

    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        this.request = request;
        enrollee_subjects_rates = new LinkedList<Subject>();

        if (chechInput()) {
            String fName = (String) this.request.getSession().getAttribute(
                    this.pageParameters.getProperty("FIRST_NAME_FORM_VALUE"));
            String lName = (String) this.request.getSession().getAttribute(
                    this.pageParameters.getProperty("LAST_NAME_FORM_VALUE"));
            String tName = (String) this.request.getSession().getAttribute(
                    this.pageParameters.getProperty("THIRD_NAME_FORM_VALUE"));
            int facultyIdValue = Integer.parseInt(this.request.getSession().getAttribute(
                    this.pageParameters.getProperty("FACULTY_ID_FROM_USER")).toString());
            String eMail = this.request.getSession().getAttribute(
                    this.pageParameters.getProperty("EMAIL_FORM_VALUE")).toString();
            int pass = getPassword();

            /* Calculates the total score of the enrollee as a sum of his subjects and avarage rate */
            double totalRate = this.averageRate;;
            Iterator<Subject> iter = this.enrollee_subjects_rates.iterator();
            while (iter.hasNext()) {
                totalRate += (double) iter.next().getSubject_mark();
            }

            /* Creates Enrollee object */
            Enrollee enrollee = new Enrollee(
                    fName, lName, tName, totalRate, facultyIdValue,
                    this.enrollee_subjects_rates);

            /* Sets the enrollee to the database */
            RegistrationDAO dao = CommandsToMySqlDAOLinker.getInstance().getRegistrationDAO(request);
            
            if (pass != 0 && dao.addEnrollee(enrollee, eMail, pass)) {

                infoLog.info("User [" + eMail + "] was successfully registered");

                return this.pageParameters.getProperty("LOGIN_PAGE");   /* If everything is OK */
            } else {
                return this.pageParameters.getProperty("ERROR_PAGE");
            }
        }
        this.averageRate = 0;
        return this.pageParameters.getProperty("REGISTER_MARKS_PAGE");  /* If the information inputted by user is not valid */
    }

    /**
     * Checks the information inputted by the user.
     *
     * @return true if valid.
     */
    private boolean chechInput() {
        /* Regular expressions */
        Pattern pattern = Pattern.compile(this.pageParameters.getProperty("GRADE_PATTERN"));
        Pattern averagePattern = Pattern.compile(this.pageParameters.getProperty("AVERAGE_GRADE_PATTERN"));
        Matcher matcher = null;
        boolean resultFlag = true;

        /* Checks the every existing field sequentially  */
        resultFlag = checkCurrentParameter(pattern, matcher, this.pageParameters.
                getProperty("UKRAINIAN_FROM_USER"),
                this.pageParameters.getProperty("UKRAINIAN_FORM_VALUE"),
                Subjects.UKRAINIAN_SUBJECT_ID.getValue(), resultFlag);
        resultFlag = checkCurrentParameter(pattern, matcher, this.pageParameters.
                getProperty("FOREIGN_FROM_USER"),
                this.pageParameters.getProperty("FOREIGN_FORM_VALUE"),
                Subjects.FOREIGN_SUBJECT_ID.getValue(), resultFlag);
        resultFlag = checkCurrentParameter(pattern, matcher, this.pageParameters.
                getProperty("MATH_FROM_USER"),
                this.pageParameters.getProperty("MATH_FORM_VALUE"),
                Subjects.MATHEMATICS_SUBJECT_ID.getValue(), resultFlag);
        resultFlag = checkCurrentParameter(pattern, matcher, this.pageParameters.
                getProperty("CHEMISTRY_FROM_USER"),
                this.pageParameters.getProperty("CHEMISTRY_FORM_VALUE"),
                Subjects.CHEMISTRY_SUBJECT_ID.getValue(), resultFlag);
        resultFlag = checkCurrentParameter(pattern, matcher, this.pageParameters.
                getProperty("PHYSICS_FROM_USER"),
                this.pageParameters.getProperty("PHYSICS_FORM_VALUE"),
                Subjects.PHYSICS_SUBJECT_ID.getValue(), resultFlag);
        resultFlag = checkCurrentParameter(pattern, matcher, this.pageParameters.
                getProperty("HISTORY_OF_UKRAINE_FROM_USER"),
                this.pageParameters.getProperty("HISTORY_OF_UKRAINE_FORM_VALUE"),
                Subjects.HISTORY_SUBJECT_ID.getValue(), resultFlag);
        resultFlag = checkAndConvertAverageRate(averagePattern, matcher, this.pageParameters.
                getProperty("AVERAGE_GRADE_FROM_USER"),
                this.pageParameters.getProperty("AVERAGE_GRADE_VALUE"), resultFlag);
        return resultFlag;
    }

    /**
     * Checks the current value.
     *
     * @param pattern regular expression.
     * @param matcher matcher for the pattern.
     * @param src session parameter name.
     * @param result session value parameter name.
     * @param initValue constant according to the this.Subjects.
     * @param resultFlag flag.
     * @return true if valid.
     */
    private boolean checkCurrentParameter(Pattern pattern, Matcher matcher, String src,
            String result, int initValue, boolean resultFlag) {
        String source = this.request.getParameter(src);

        if (source != null) {           /* If such field does not exist */
            matcher = pattern.matcher(source);

            if (matcher.matches()) {    /* If matches the regular expression */
                int gradeValue = Integer.parseInt(source);

                /* If the value in the right range */
                if (gradeValue >= this.MIN_GRADE_VALUE && gradeValue <= this.MAX_GRADE_VALUE) {
                    this.request.setAttribute(result, gradeValue);  /*Sets the session attribute */
                    enrollee_subjects_rates.add(new Subject(initValue, gradeValue));
                    return resultFlag;
                }
            }
            this.request.setAttribute(this.pageParameters.getProperty("ERROR_ATTR"), "true");
            return false;
        }
        return resultFlag;
    }

    /**
     * Checks the average grage value.
     *
     * @param pattern regular expression.
     * @param matcher matcher for the pattern.
     * @param src session parameter name.
     * @param result session value parameter name.
     * @param resultFlag flag.
     * @return true if valid.
     */
    private boolean checkAndConvertAverageRate(Pattern pattern, Matcher matcher, String src,
            String result, boolean resultFlag) {
        String source = this.request.getParameter(src);

        if (source != null) {           /* If such field does not exist */
            matcher = pattern.matcher(source);

            if (matcher.matches()) {    /* If matches the regular expression */
                double gradeValue = Double.parseDouble(source);

                /* If the value in the right range */
                if (gradeValue >= this.MIN_VALUE_AVERAGE && gradeValue <= this.MAX_VALUE_AVERAGE) {
                    this.request.setAttribute(result, gradeValue);  /*Sets the session attribute */

                    /* Converts the information inputted in the 200-score format */
                    double convertedValue = Double.parseDouble(this.conversionEstimates.
                            getProperty(String.valueOf(gradeValue)));
                    if (convertedValue >= this.MIN_VALUE_AVERAGE_200
                            && convertedValue <= this.MAX_VALUE_AVERAGE_200) {
                        this.averageRate = convertedValue;
                        return resultFlag;
                    }
                }
            }
        }
        this.request.setAttribute(this.pageParameters.getProperty("ERROR_ATTR"), "true");
        return false;
    }

    /**
     * Gets the user password in the encrypted format from the session.
     *
     * @return password.
     */
    private int getPassword() {
        int pass = 0;
        try {
            pass = (Integer) (this.request.getSession().getAttribute(
                    this.pageParameters.getProperty("PASSWORD_FOR_TRANSMISSION")));
        } catch (NumberFormatException ex) {
            log.error(ex);
        }
        return pass;
    }
}
