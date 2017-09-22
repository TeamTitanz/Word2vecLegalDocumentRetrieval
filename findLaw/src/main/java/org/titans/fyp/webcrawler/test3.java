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
package org.titans.fyp.webcrawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.titans.fyp.webcrawler.database.DBHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Buddhi on 6/11/2017.
 */
public class test3 {

    private static Logger log = LoggerFactory.getLogger(GraphGenerator.class);
    ExtractNames ens = new ExtractNames();

//    private List<String> nameList = new ArrayList<String>();
//    private List<String> contentList = new ArrayList<String>();


//    public String getCases() throws IOException {
//        ResultSet rs;
//        String str = "SELECT name, content FROM cases";
//        try {
//            Connection connection = DBConnection.getConnection();
//            rs = DBHandler.getData(str, connection);
//            while (rs.next()) {
//                rs.next();
//                try {
//                    String name = rs.getString("name");
//                    nameList.add(name);
//
//                    String content = rs.getString("content");
//                    contentList.add(content);
//
//                } catch (SQLException ex) {
//                    log.error(ex.getMessage());
//                }
//            }
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        return null;
//    }

    public String getContent(Connection dbConnection, String case_id, BufferedWriter bw) {

        String str = "SELECT content FROM cases_content WHERE case_id=" + case_id;
        ResultSet rst;
        List<String> nameList;
        StringBuilder sb = new StringBuilder();

        try {
            rst = DBHandler.getData(str, dbConnection);
            rst.next();
            while (rst.next()) {
                try {
                    String content = rst.getString("content");
                    String[] parts = content.split("\"");
                    for (String tem : parts) {
                        if (tem.contains(" v. ")) {
                            System.out.println(tem);
                            nameList = ens.getNames(tem);
                            if (nameList.size() > 0) {
                                for (String mName : nameList) {
                                    sb.append(mName);
                                    sb.append("_=,=_");
                                }

                            }
                        }
                    }
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
            String temNL = sb.toString();
//            System.out.println(temNL.substring(0, (temNL.length() - 5)));
            bw.write(temNL.substring(0, (temNL.length() - 5)) + "\n");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void createNameGraphCSV() {

        Connection dbConnection;
        String dbUserName = "root";
        String dbPassword = "root";
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(System.getProperty("user.dir") + File.separator + "NameGraph.csv");
            bw = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int oblie_db = 74; oblie_db <= 74; oblie_db++) {
            if (oblie_db != 46 && oblie_db != 48) {

                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oblie_" + Integer.toString(oblie_db)
                            //database access credentials
                            , dbUserName, dbPassword);

                    String str1 = "SELECT case_id, Name FROM cases";
                    ResultSet rs1 = DBHandler.getData(str1, dbConnection);

                    while (rs1.next()) {
                        try {
                            String name = rs1.getString("name");
                            bw.write(name + "_=;=_");

                            String case_id = rs1.getString("case_id");
                            getContent(dbConnection, case_id, bw);
                            System.out.println();
                        } catch (SQLException ex) {
                            log.error(ex.getMessage());
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }
                    dbConnection.close();

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        test3 gg = new test3();
        gg.createNameGraphCSV();
    }


}
