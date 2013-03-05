/*
 * @(#)ChangeLanguageCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * ControllerCommand object. Provides options to change language of the view
 * pages.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class GlobalChangeLanguage implements ControllerCommand {

    /** Constants, that represent session attributes */
    private static final String LANGUAGE_PARAMETER = "lang";
    private static final String URL_PARAMETER = "link";
    private static final String EN_LOCALE = "en";
    private static final String RU_LOCALE = "ru";

    /**
     * Provides options to change language of the view pages.
     *
     * @param request
     * @return Adress of the redirect page.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        String path = request.getParameter(URL_PARAMETER); /* Page from which request has been received */
        String lang = request.getParameter(LANGUAGE_PARAMETER);
        if (lang.equals(EN_LOCALE)) {
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(300);
            session.setAttribute(LANGUAGE_PARAMETER, EN_LOCALE);
        } else {
            HttpSession session = request.getSession(true);
            session.setMaxInactiveInterval(300);
            session.setAttribute(LANGUAGE_PARAMETER, RU_LOCALE);
        }
        return path;
    }
}
