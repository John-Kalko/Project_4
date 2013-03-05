/*
 * @(#)SetCharFilter.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;

/**
 * Charset filter. Converts to UTF-8 format.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalako Ievgen.
 */
public class SetCharFilter implements Filter {

    /**
     * Log4j object
     */
    private final static Logger log = Logger.getLogger(SetCharFilter.class.getName());
    private FilterConfig filterConfig = null;

    /**
     * Init method for this filter.
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Converts charset to UTF-8 if it is different.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        try {
            String encoding = request.getCharacterEncoding();
            if (!"UTF-8".equalsIgnoreCase(encoding)) { /*If the charset is different than UTF-8 */
                request.setCharacterEncoding("UTF-8");
            }
            chain.doFilter(request, response);
        } catch (ServletException ex) {
            log.error("Controller exception. ", ex);
        } catch (IOException ex) {
            log.error("Controller exception. ", ex);
        } catch (Exception ex) {
            log.error("Controller exception. ", ex);
        }
    }

    /**
     * Destroy method for this filter.
     */
    @Override
    public void destroy() {
    }
}
