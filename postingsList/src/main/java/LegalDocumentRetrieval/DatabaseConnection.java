/*******************************************************************************
 * Copyright 2016 Titans
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 ******************************************************************************/
package LegalDocumentRetrieval;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Buddhi Ayesha
 */
public class DatabaseConnection {

    private Connection connection;
    private static DatabaseConnection dBConnection;
    private static String host = "jdbc:mysql://127.0.0.1:3306/";
    private static String username = "root";
    private static String password = "root";

    public DatabaseConnection(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(host + dbName, username, password);
    }

    public Connection getConnectionToDB() {
        return connection;
    }

    public static DatabaseConnection getDBConnection(String dbName) throws ClassNotFoundException, SQLException {
        if (dBConnection == null) {
            try {
                dBConnection = new DatabaseConnection(dbName);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return dBConnection;
    }


}
