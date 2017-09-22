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

import org.apache.log4j.Logger;
import org.titans.fyp.webcrawler.models.Case;
import org.titans.fyp.webcrawler.models.CaseInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBHandler {

    final static Logger logger = Logger.getLogger(DBHandler.class);

    //use to write case content data to database
    public static boolean setCaseContent(Case caseCon) {

        PreparedStatement stmt;
        try {

            Connection connection = DBConnection.getConnection();
            stmt = connection.prepareStatement("INSERT INTO cases_content (case_id, content) VALUES(?,?)");
            stmt.setInt(1, caseCon.getCaseId());
            stmt.setString(2, caseCon.getContent());
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Case content Error -> CaseId: " + caseCon.getCaseId() + "\n" + e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean setCaseInfo(CaseInfo caseInfo) {

        PreparedStatement stmt;
        try {

            Connection connection = DBConnection.getConnection();
            stmt = connection.prepareStatement("INSERT INTO cases VALUES(?,?,?,?,?,?)");
            stmt.setInt(1, caseInfo.getCaseId());
            stmt.setString(2, caseInfo.getRealName());
            stmt.setString(3, caseInfo.getName());
            stmt.setString(4, caseInfo.getSummaryPageURL());
            stmt.setString(5, caseInfo.getReadPageURL());
            stmt.setString(6, caseInfo.getSummary());
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Case Info Error -> " + "SummaryPage:" + caseInfo.getSummaryPageURL() + "\tReadPage:"
                    + caseInfo.getReadPageURL() + "\n" + e.getMessage());
            return false;
        }
        return true;
    }

    //use to read data from database
    public static ResultSet getData(String query, Connection connection) throws SQLException {
        java.sql.Statement stm = connection.createStatement();
        ResultSet res = stm.executeQuery(query);
        return res;
    }


}
