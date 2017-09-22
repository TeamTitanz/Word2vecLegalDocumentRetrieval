package org.titans.fyp.webcrawler;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

import java.util.Properties;

/**
 * Created by Buddhi on 6/12/2017.
 */
public class NLPSeverTest {

    public void test(){

        // creates a StanfordCoreNLP object with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLPClient pipeline = new StanfordCoreNLPClient(props, "localhost", 9000, 4);
// read some text in the text variable
        String text = "Add your text here"; // Add your text here!
// create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
// run all Annotators on this text
        pipeline.annotate(document);

    }

    public static void main(String[] args) {
        NLPSeverTest nt =new NLPSeverTest();
        nt.test();
    }

}
