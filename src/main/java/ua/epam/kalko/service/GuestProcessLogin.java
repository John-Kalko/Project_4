/*
 * @(#)ProcessLoginCommand.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ua.epam.kalko.service.util.CommandsToMySqlDAOLinker;
import ua.epam.kalko.integration.GuestDAO;

/**
 * ControllerCommand object. Provides options for user authenification.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class GuestProcessLogin implements ControllerCommand {

    private final static Logger infoLog = Logger.getLogger("InfoLog");
    private final String ERROR_PAGE = "guest_login.jspx";
    private final String ADMIN_INDEX = "admin_index.jspx";
    private final String USER_INDEX = "ControllerMain?command=ENROLLEE_GET_RESULT";
    private final String USER_LOGIN_ATTR = "user_login";
    private final int ADMIN_CHECK = 234598275;
    private final int ENROLLEE_CHECK = 452950295;

    /**
     * Provides options for user authenification.
     *
     * @param request current request from the servlet.
     * @return redirect page adress.
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request) throws ServletException, IOException {
        String login = request.getParameter("e_mail");
        String sourcePass = request.getParameter("password");       /* Not encrypted password */
        int pass = sourcePass.hashCode() >> 2;                      /* Encryption algorithm */
        GuestDAO.UserLevel level;
        GuestDAO dao = CommandsToMySqlDAOLinker.getInstance().getGusetDAO(request);

        level = dao.authenificate(login, pass);
        if (level == null) {                                        /* If no mathes */
            request.setAttribute("error_type", "true");
            return ERROR_PAGE;
        } else if (level.equals(GuestDAO.UserLevel.ADMIN)) {        /* If admin */
            HttpSession adminSession = createNewSession(request);
            adminSession.setAttribute("admin_check", (ADMIN_CHECK >>> 10));
            /* Code to protect admin pages from invalid users */
            infoLog.info("Admin [" + login + "] authenificates. Session ID: "
                    + request.getSession().getId());
            return ADMIN_INDEX;
        }                                                           /* If user */
        HttpSession userSession = createNewSession(request);
        /* Code to protect user page from invalid users */
        userSession.setAttribute("user_check", (ENROLLEE_CHECK >>> 10));
        request.getSession().setAttribute(this.USER_LOGIN_ATTR, login);
        infoLog.info("Enrollee [" + login + "] authenificates. Session ID: "
                + request.getSession().getId());
        return USER_INDEX;
    }

    /**
     * Invalidates current session and creates new one.
     *
     * @param request current request.
     * @return current session.
     */
    private HttpSession createNewSession(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        /* Stores locale value from the previous session */
        String lang = (String) session.getAttribute("lang");
        session.invalidate();
        HttpSession newSession = request.getSession();
        session.setMaxInactiveInterval(300);
        newSession.setAttribute("lang", lang);
        return newSession;
    }
}
