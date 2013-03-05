/*
 * @(#)RequestToCommandLinker.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.web;

import java.util.HashMap;
import ua.epam.kalko.service.*;

/**
 * Stores ControllerCommand objects and links them with the respective string
 * names. Singleton class.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen
 */
public class RequestToCommandLinker {

    /** Log44j object */
    private static RequestToCommandLinker linker = new RequestToCommandLinker();
    /** Stores ControllerCommand objects */
    private HashMap<String, ControllerCommand> command;

    /** Initializes commands */
    private RequestToCommandLinker() {
        this.command = new HashMap<String, ControllerCommand>();
        this.command.put("ADMIN_GET_ENROLLES", new AdminGetEnrollees());
        this.command.put("ADMIN_APPLY_ENROLLEE", new AdminApplyEnrollee());
        this.command.put("ADMIN_GET_QUALIFIED_ENROLLES", new AdminGetQualifiedEnrollees());
        this.command.put("ADMIN_APPLY_REPORT", new AdminApplyReport());
        this.command.put("ENROLLEE_GET_RESULT", new EnrolleeGetResult());
        this.command.put("ENROLLEE_GET_FACULTIES", new EnrolleeGetFaculties());       
        this.command.put("GLOBAL_NO_COMMAND", new GlobalNoCommand());
        this.command.put("GLOBAL_CHANGE_LANGUAGE", new GlobalChangeLanguage());
        this.command.put("GLOBAL_LOG_OUT", new GlobalLogOut());
        this.command.put("GUEST_PROCESS_LOGIN", new GuestProcessLogin());
        this.command.put("GUEST_GET_FACULTIES", new GuestGetFaculties());
        this.command.put("REGISTRATION_PAGE_2", new RegistrationPage2());
        this.command.put("REGISTRATION_GET_FACULTIES", new RegistrationGetFaculties());
    }

    /**
     * Returnes ControllerCommand object by the String name.
     *
     * @param commandName command name.
     * @return ControllerCommand.
     */
    public static ControllerCommand getCommand(String commandName) {

        ControllerCommand command = RequestToCommandLinker.linker.command.
                get(commandName);
        if (command == null) {
            /* Lazy initialization for the concurrent execution */
            if(commandName.equals("REGISTRATION_PAGE_1")) { 
                return new RegistrationPage1();
            }
            /* Command, which returnes error page */
            return RequestToCommandLinker.linker.command.get("GLOBAL_NO_COMMAND");
        }
        return command;
    }
}
