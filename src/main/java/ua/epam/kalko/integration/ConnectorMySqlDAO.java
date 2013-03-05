/*
 * @(#)ConnectorMySqlDAO.java      0.1 13/01/16
 * 
 * The Admissions Committee Web System.
 * Kiev, Ukraine.
 */
package ua.epam.kalko.integration;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 * DAO Connection singleton class.
 *
 * @version 0.1 16 Jan 2013.
 * @author Kalko Ievgen.
 */
public class ConnectorMySqlDAO {

    /**
     * Logger object to make error logs.
     */
    private final static Logger log = Logger.getLogger(ConnectorMySqlDAO.class.getName());
    /**
     * Stores JNDI name of the database connection pool.
     */
    private static final String POOL = "university_pool";
    /**
     * Stores reference to the connection object.
     */
    private static Connection currentConnection;

    private ConnectorMySqlDAO() { }

    /**
     * Creates connection object.
     * @return new connection object.
     */
    public static Connection getConnection() {
        try {
            
            /* Creates connection if it's not exist or being closed */
            if (ConnectorMySqlDAO.currentConnection == null
                    || ConnectorMySqlDAO.currentConnection.isClosed()) {
                Context ct = new InitialContext();
                DataSource ds = (DataSource) ct.lookup(POOL);
                return ds.getConnection();
            }
        } catch (SQLException ex) {
            log.error("DAO Exception. ", ex);
            return null;
        } catch (NullPointerException ex) {
            log.error("DAO Exception. ", ex);
            return null;
        } catch (NamingException ex) {
            log.error("DAO Exception. ", ex);
            return null;
        }
        return ConnectorMySqlDAO.currentConnection;
    }
}
