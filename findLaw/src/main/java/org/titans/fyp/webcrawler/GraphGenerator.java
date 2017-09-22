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

import org.apache.log4j.Logger;
import org.titans.fyp.webcrawler.database.DBConnection;
import org.titans.fyp.webcrawler.database.DBHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi on 6/11/2017.
 */
public class GraphGenerator {

    private static Logger logger = Logger.getLogger(GraphGenerator.class);
    ExtractNames ens = new ExtractNames();
    private String dbUserName = "root";
    private String dbPassword = "root";

    public String getCases() throws IOException {
        ResultSet rs;
        String str = "SELECT name, content FROM cases";
        try {
            Connection connection = DBConnection.getConnection();
            rs = DBHandler.getData(str, connection);
            while (rs.next()) {
                rs.next();
                try {
                    String name = rs.getString("name");
//                    nameList.add(name);

                    String content = rs.getString("content");
//                    contentList.add(content);

                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    private List<String> simplify(String sentence, int sLen) {
        int minI;
        int maxI;
        int len = 100;
        String regex = " v. ";
        List<String> sList = new ArrayList<>();

        int index = sentence.indexOf(regex);
        while (index >= 0) {
            if (0 < (index - len)) {
                minI = index - len;
            } else {
                minI = 0;
            }
            if ((index + len) < sLen) {
                maxI = index + len;
            } else {
                maxI = sLen;
            }
            sList.add(sentence.substring(minI, maxI));
            index = sentence.indexOf(regex, index + 1);
        }
        return sList;
    }

    public void getContent(Connection dbConnection, String case_id, BufferedWriter bw) {

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
                            int sLen = tem.length();
                            if (sLen > 300) {
                                List<String> sList = simplify(tem, sLen);
                                for (String sltem : sList) {
                                    nameList = ens.getNames(sltem);
                                    if (nameList.size() > 0) {
                                        for (String mName : nameList) {
                                            sb.append(mName);
                                            sb.append("_=,=_");
                                        }
                                    }
                                }
                            } else {
                                nameList = ens.getNames(tem);
                                if (nameList.size() > 0) {
                                    for (String mName : nameList) {
                                        sb.append(mName);
                                        sb.append("_=,=_");
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                }
            }
            String temNL = "";
            temNL += sb.toString();
            int temNLLen = temNL.length();
            if ((temNLLen - 5) > 5) {
                temNL = temNL.substring(0, (temNLLen - 5));
            }
            bw.write(temNL + "\n");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createNameGraphCSV(int startDB, int endDB) {
        Connection dbConnection;
        FileWriter fw = null;
        BufferedWriter bw = null;
        int caseCount = 0;

        for (int oblie_db = startDB; oblie_db <= endDB; oblie_db++) {
            logger.info("Name extracting from oblie_" + oblie_db + "...");
            try {
                fw = new FileWriter(System.getProperty("user.dir") + File.separator + "NameGraph" + oblie_db + ".csv");
                bw = new BufferedWriter(fw);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (oblie_db != 46 && oblie_db != 48) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oblie_"
                                    + Integer.toString(oblie_db)
                            //database access credentials
                            , dbUserName, dbPassword);

                    String str1 = "SELECT case_id, Name, readPageURL FROM cases";
                    ResultSet rs1 = DBHandler.getData(str1, dbConnection);

                    while (rs1.next()) {
                        logger.info("Cases Count = " + caseCount++);
                        try {
                            String readPage = rs1.getString("readPageURL");
                            bw.write(readPage + "_=r=_");

                            String name = rs1.getString("name");
                            bw.write(name + "_=;=_");

                            String case_id = rs1.getString("case_id");
                            getContent(dbConnection, case_id, bw);
                        } catch (SQLException ex) {
                            logger.error(ex.getMessage());
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
                    dbConnection.close();

                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage());
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
            }

            try {
                if (fw != null)
                    fw.close();
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

    }

    public void setDbUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public static void main(String[] args) {

        int startDB = 58;
        int endDB = 60;
        GraphGenerator gg = new GraphGenerator();

        if (args.length > 1) {
            startDB = Integer.parseInt(args[0]);
            endDB = Integer.parseInt(args[1]);
            gg.setDbUserName(args[2]);
            logger.info("Database UserName: " + args[2]);
            gg.setDbPassword(args[3]);
        }

        gg.createNameGraphCSV(startDB, endDB);
    }

}
