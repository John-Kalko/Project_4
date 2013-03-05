/*
 * @(#)CheckRegistrationFormCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;
import ua.epam.kalko.integration.RegistrationDAO;
import ua.epam.kalko.domain.Subject;

/**
 * ControllerCommand object. Provides validation of the user registration form.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class RegistrationPage1 implements ControllerCommand {

    /**
     * Logger object to make error logs.
     */
    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.
            getLogger(RegistrationPage2.class.getName());
    /**
     * Path to the properties file with the *.jspx page parameters.
     */
    private final String PAGE_PARAMETERS_PATH = "../../../../Commands.properties";
    Properties pageParameters;
    private HttpServletRequest request;
    /**
     * Stores user inputted values.
     */
    private String eMailValue;
    private String passwordValue;
    private String passwordToConfirmValue;
    private String firstNameValue;
    private String lastNameValue;
    private String thirdNameValue;
    private int facultyIdValue;
    private int cryptedPasswordValue;

    private RegistrationDAO dao;
    
    /**
     * Use ControllerCommand interface to invoke method execute(). Initializes
     * link to the properties file with the constants.
     */
    public RegistrationPage1() {
        pageParameters = new Properties();
        try {
            pageParameters.load(getClass().getResourceAsStream(this.PAGE_PARAMETERS_PATH));
        } catch (IOException ex) {
            log.error("Command error. Properties file loading fails", ex);
        }
    }

    /**
     * Initializes link to the properties file with the constants and invokes
     * validation methods.
     *
     * @param request
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        this.request = request;
        this.eMailValue = request.getParameter(pageParameters.getProperty("E_MAIL_FROM_USER"));
        this.passwordValue = request.getParameter(pageParameters.getProperty(
                "PASSWORD_FROM_USER"));
        this.passwordToConfirmValue = request.getParameter(pageParameters.
                getProperty("CONFIRM_PASSWORD_FROM_USER"));
        this.firstNameValue = request.getParameter(pageParameters.getProperty(
                "FIRST_NAME_FROM_USER"));
        this.lastNameValue = request.getParameter(pageParameters.getProperty(
                "LAST_NAME_FROM_USER"));
        this.thirdNameValue = request.getParameter(pageParameters.getProperty(
                "THIRD_NAME_FROM_USER"));
        this.facultyIdValue = Integer.parseInt(request.getParameter(pageParameters.
                getProperty("FACULTY_ID_FROM_USER")));

        this.dao = CommandsToMySqlDAOLinker.getInstance().getRegistrationDAO(request);
        
        if (!checkInput()) {
            return pageParameters.getProperty("REGISTER_PAGE");
        }

        /* Initializes enrollee and his subjects */
        ArrayList<Subject> subjectsForFaculty = new ArrayList<Subject>(this.dao.
                getSubjects(this.facultyIdValue));
        HttpSession session = request.getSession(true);
        session.setAttribute(pageParameters.getProperty("SUBJECT_LIST_FORM_VALUE"),
                subjectsForFaculty);
        session.setAttribute(pageParameters.getProperty("FACULTY_ID_FROM_USER"),
                this.facultyIdValue);
        session.setAttribute(pageParameters.getProperty("PASSWORD_FOR_TRANSMISSION"),
                this.cryptedPasswordValue);
        return pageParameters.getProperty("REGISTER_MARKS_PAGE");
    }

    /**
     * Sequentially checks user inputted data according to the patterns. If just
     * one value fails, all others would be posted to the *.jspx page.
     *
     * @return true if successful.
     */
    private boolean checkInput() {
        boolean result = true;
        if (!checkEMail()) {
            result = false;
        }
        if (!checkPassword()) {
            result = false;
        }
        if (!checkName(this.firstNameValue)) {
            request.setAttribute(pageParameters.getProperty("FNAME_ERROR"), "true");
            result = false;
        } else {        /*Sets the resulted value */
            request.getSession().setAttribute(pageParameters.getProperty(
                    "FIRST_NAME_FORM_VALUE"), this.firstNameValue);
        }
        if (!checkName(this.lastNameValue)) {
            request.setAttribute(pageParameters.getProperty("LNAME_ERROR"), "true");
            result = false;
        } else {        /*Sets the resulted value */
            request.getSession().setAttribute(pageParameters.getProperty(
                    "LAST_NAME_FORM_VALUE"), this.lastNameValue);
        }
        if (!checkName(this.thirdNameValue)) {
            request.setAttribute(pageParameters.getProperty("THAME_ERROR"), "true");
            result = false;
        } else {        /*Sets the resulted value */
            request.getSession().setAttribute(pageParameters.getProperty(
                    "THIRD_NAME_FORM_VALUE"), this.thirdNameValue);
        }
        return result;
    }

    /**
     * Checks the e-mail, inputted by user according to the pattern, and sets
     * 'error' attribute for the *.jspx page if validation fails.
     *
     * @return true if successful.
     */
    private boolean checkEMail() {
        Pattern pattern = Pattern.compile(pageParameters.getProperty("EMAIL_PATTERN"));
        Matcher matcher = pattern.matcher(this.eMailValue);
        if (matcher.matches()) {
            if (!this.dao.searchLogin(this.eMailValue)) {
                request.getSession().setAttribute( /*Sets the resulted value */
                        pageParameters.getProperty("EMAIL_FORM_VALUE"), this.eMailValue);
                return true;
            } else {
                request.setAttribute(pageParameters.getProperty("EMAIL_DUPLICATION_ERROR"), "true");
            }
        } else {
            request.setAttribute(pageParameters.getProperty("EMAIL_ERROR"), "true");
        }
        return false;
    }

    /**
     * Checks all the receiving 'names' according to the pattern and sets
     * 'error' attribute for the *.jspx page if validation fails.
     *
     * @param name String name to validate.
     * @return true if successful.
     */
    private boolean checkName(String name) {
        Pattern pattern = Pattern.compile(pageParameters.getProperty("NAME_PATTERN"),
                Pattern.UNICODE_CASE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * Checks the passwords, inputted by user according to the pattern and for
     * equality with each other. Can set 'error' attribute for the *.jspx page
     * if validation fails.
     *
     * @return true if successful.
     */
    private boolean checkPassword() {
        Pattern pattern = Pattern.compile(pageParameters.getProperty("PASSWORD_PATTERN"));
        Matcher matcher = pattern.matcher(this.passwordValue);
        if (this.passwordValue.length() >= 7 && this.passwordValue.length() <= 20) {
            if (matcher.matches()) {    /*Sets the resulted value */
                if (this.passwordValue.equals(this.passwordToConfirmValue)) {
                    this.cryptedPasswordValue = this.passwordValue.hashCode() >> 2;
                    return true;
                } else {
                    request.setAttribute(pageParameters.getProperty("PASSWORD_CONFIRM_ERROR"),
                            "true");
                }
            } else {
                request.setAttribute(pageParameters.getProperty("PASSWORD_ERROR"), "true");
            }
        } else {
            request.setAttribute(pageParameters.getProperty("PASSWORD_LENGTH_ERROR"), "true");
        }
        return false;
    }
}
