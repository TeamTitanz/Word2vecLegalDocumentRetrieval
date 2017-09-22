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
package org.titans.fyp.webcrawler.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection establishment happens here.
 */
public class DBConnection {

    private static Connection dbConnection;
    private static String dbCaseName = "";
    private static String dbUserName = "root";
    private static String dbPassword = null;

    private DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oblie" + dbCaseName
                //database access credentials
                , dbUserName, dbPassword);
    }

    public static void setDbCaseName(String dbCaseName) {
        DBConnection.dbCaseName = dbCaseName;
    }

    public static void setDbUserName(String dbUserName) {
        DBConnection.dbUserName = dbUserName;
    }

    public static void setDbPassword(String dbPassword) {
        DBConnection.dbPassword = dbPassword;
    }

    public static Connection getConnection() {
        try {
            if (dbConnection == null) {
                new DBConnection(); //make new database
            }
            return dbConnection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
