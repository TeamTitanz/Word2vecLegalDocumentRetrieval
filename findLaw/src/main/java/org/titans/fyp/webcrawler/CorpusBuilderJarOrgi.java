package org.titans.fyp.webcrawler;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.Logger;
import org.titans.fyp.webcrawler.database.DBHandler;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Buddhi on 6/13/2017.
 */
public class CorpusBuilderJarOrgi {

    private static Logger logger = Logger.getLogger(CorpusBuilderJar.class);
    private String dbUserName = "root";
    private String dbPassword = "root";

    public String LemmatizeAndWriteToFile(String text) throws IOException {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        StringBuilder sb = new StringBuilder();
        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the lemmatized version of the token
                String word = token.get(CoreAnnotations.LemmaAnnotation.class);
                if (String.valueOf(word).chars().allMatch(Character::isLetter)) {
                    if (word != null) {
                        sb.append(String.valueOf(word.toLowerCase()));
                        sb.append(" ");
                    }
                }
            }
        }
        return sb.toString();
    }

    public String tokenTextWriteToFile(String text) throws IOException {

        // creates a StanfordCoreNLP object, with Tokenizer
        PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<CoreLabel>(new StringReader(text),
                new CoreLabelTokenFactory(), "");
        StringBuilder sb = new StringBuilder();
        for (CoreLabel label; ptbt.hasNext(); ) {
            label = ptbt.next();
            if (String.valueOf(label).chars().allMatch(Character::isLetter)) {
                sb.append(String.valueOf(label).toLowerCase());
                sb.append(" ");
            } else if (String.valueOf(label).equals(".")) {
                try {
                    int len = sb.toString().length();
                    sb.deleteCharAt(len - 1);
                    sb.append(label);
                } catch (Exception e) {
                }
            } else if (String.valueOf(label).equals(",")) {
                try {
                    int len = sb.toString().length();
                    sb.deleteCharAt(len - 1);
                    sb.append(label);
                } catch (Exception e) {
                }
            }
        }
        return sb.toString();
    }

    public void getContent(Connection dbConnection, String case_id, BufferedWriter bw_raw, BufferedWriter bw_lemm, BufferedWriter bw_token) {

        String str = "SELECT content FROM cases_content WHERE case_id=" + case_id;
        ResultSet rst;

        try {
            rst = DBHandler.getData(str, dbConnection);
            rst.next();
            while (rst.next()) {
                try {
                    String content = rst.getString("content");
                    String[] parts = content.split("\"");
                    StringBuilder sb = new StringBuilder();
                    for (String tem : parts) {
                        bw_lemm.write(LemmatizeAndWriteToFile(tem));
                        String tt = tokenTextWriteToFile(tem);
                        sb.append(tt);
                        bw_token.write(tt.replaceAll("[,.]", ""));
                    }
                    String raw_str = sb.toString();
                    int len = raw_str.length();
                    if (len > 1 && sb.charAt(len - 1) != '\n') {
                        sb.append("\n");
                    }
                    if (len > 10) {
                        bw_raw.write(sb.toString());
                    }
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    public void createCorpusBuilder(int startDB, int endDB) {

        Connection dbConnection;

        for (int oblie_db = startDB; oblie_db <= endDB; oblie_db++) {
            int caseCount = 0;
            logger.info("Corpus Build from oblie_" + oblie_db + "...");
            if (oblie_db != 46 && oblie_db != 48) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/oblie_"
                                    + Integer.toString(oblie_db)
                            //database access credentials
                            , dbUserName, dbPassword);

                    String str1 = "SELECT case_id FROM cases";
                    ResultSet rs1 = DBHandler.getData(str1, dbConnection);

                    while (rs1.next()) {
                        try {
                            File file_raw = new File(System.getProperty("user.dir") + File.separator + "Text" + File.separator + "RawText" + File.separator + oblie_db + File.separator + "case" + caseCount + ".txt");
                            file_raw.getParentFile().mkdirs();
                            FileWriter fw_raw = new FileWriter(file_raw);
                            BufferedWriter bw_raw = new BufferedWriter(fw_raw);

                            File file_lemm = new File(System.getProperty("user.dir") + File.separator + "Text" + File.separator + "LemmatizedText" + File.separator + oblie_db + File.separator + "case" + caseCount + ".txt");
                            file_lemm.getParentFile().mkdirs();
                            FileWriter fw_lemm = new FileWriter(file_lemm);
                            BufferedWriter bw_lemm = new BufferedWriter(fw_lemm);

                            File file_token = new File(System.getProperty("user.dir") + File.separator + "Text" + File.separator + "TokenText" + File.separator + oblie_db + File.separator + "case" + caseCount + ".txt");
                            file_token.getParentFile().mkdirs();
                            FileWriter fw_token = new FileWriter(file_token);
                            BufferedWriter bw_token = new BufferedWriter(fw_token);

                            String case_id = rs1.getString("case_id");
                            getContent(dbConnection, case_id, bw_raw, bw_lemm, bw_token);

//                            if (fw_raw != null)
//                                fw_raw.close();
//                            if (bw_raw != null)
//                                bw_raw.close();
//
//                            if (fw_lemm != null)
//                                fw_lemm.close();
//                            if (bw_lemm != null)
//                                bw_lemm.close();
//
//                            if (fw_token != null)
//                                fw_token.close();
//                            if (bw_token != null)
//                                bw_token.close();

                            logger.info("Cases Count = " + caseCount);
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                        caseCount++;
                    }
                    dbConnection.close();

                } catch (ClassNotFoundException e) {
                    logger.error(e.getMessage());
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
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

        int startDB = 1;
        int endDB = 78;
        CorpusBuilderJar cb = new CorpusBuilderJar();

        if (args.length == 4) {
            startDB = Integer.parseInt(args[0]);
            endDB = Integer.parseInt(args[1]);
            cb.setDbUserName(args[2]);
            logger.info("Database UserName: " + args[2]);
            cb.setDbPassword(args[3]);
        }
        cb.createCorpusBuilder(startDB, endDB);
    }

}
