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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Buddhi Ayesha
 */
public class PostingsList {

    private static Logger log = LoggerFactory.getLogger(PostingsList.class);
    private static DatabaseConnection dbconnection = null;
    private static final String dbName = "oblie";
    private List<String> caseIDList = new LinkedList<String>();
    private final int fixedIDLength = 10;


    public List<String> getCasesContent(String caseID) throws IOException {
        ResultSet rs;
        String str = "SELECT case_no, content FROM cases";
        List<String> postingList = new LinkedList<String>();
        try {
            rs = DatabaseAccess.getData(dbconnection.getConnectionToDB(), str);
            while (rs.next()) {
                try {
                    String content = rs.getString("content");
                    String refCaseID = rs.getString("case_no");
                    if (content.contains(caseID)) {
                        postingList.add(refCaseID);
                    }
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return postingList;
    }

    public void getCaseID() throws IOException {
        ResultSet rs;
        String str = "SELECT case_no FROM cases";
        try {
            rs = DatabaseAccess.getData(dbconnection.getConnectionToDB(), str);
            while (rs.next()) {
                rs.next();
                try {
                    String content = rs.getString("case_no");
                    caseIDList.add(content);
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }

    private String getCorrectID(String caseID) {
        String[] parts = caseID.split("-");
        int leftLength = parts[0].length();
        int rightLength = parts[0].length();

        if (leftLength < (fixedIDLength-6)) {
            parts[0] = String.join("", Collections.nCopies((fixedIDLength-6) - leftLength, "0")) + parts[0];
        }

        if (rightLength < 6) {
            parts[1] = parts[1] + String.join("", Collections.nCopies(6 - rightLength, "0"));
        }

        return parts[0] + parts[1];
    }

    public static void main(String[] args) {

        String currentDirectory = System.getProperty("user.dir");
        BufferedWriter bw;
        FileWriter fw;
        PostingsList pl = new PostingsList();

        try {
            dbconnection = DatabaseConnection.getDBConnection(dbName);
            pl.getCaseID();
            fw = new FileWriter(currentDirectory + File.separator + "OBGraph.txt");
            bw = new BufferedWriter(fw);

            for (int x = 0; x < pl.caseIDList.size(); x++) {
                String caseID = pl.caseIDList.get(x);
                List<String> postingList = new LinkedList<String>();

                bw.write(pl.getCorrectID(caseID) + ";");
                try {
                    postingList = pl.getCasesContent(caseID);
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }

                for (String id : postingList) {
                    bw.write(pl.getCorrectID(id) + ",");
                }
            }

            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }

        } catch (IOException e) {
            log.error(String.valueOf(e));
        } catch (SQLException e) {
            log.error(String.valueOf(e));
        } catch (ClassNotFoundException e) {
            log.error(String.valueOf(e));
        }

    }

}
