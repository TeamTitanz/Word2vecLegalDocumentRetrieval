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

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.Tree;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Buddhi on 6/11/2017.
 */
public class test4 {

    private final static String PCG_MODEL = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
    private final static TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "invertible=true");
    private final static LexicalizedParser parser = LexicalizedParser.loadModel(PCG_MODEL);
    private static List<CoreLabel> tokens;
    private static Tree tree;
    private static List<Tree> leaves;

//    private Tree parse(String str) {
//        List<CoreLabel> tokens = tokenize(str);
//        Tree tree = parser.apply(tokens);
//        return tree;
//    }

//    private List<CoreLabel> tokenize(String str) {
//        Tokenizer<CoreLabel> tokenizer = tokenizerFactory.getTokenizer(new StringReader(str));
//        return tokenizer.tokenize();
//    }

//    private List<CoreLabel> simplify(List<CoreLabel> to){
//
//    }

    public List<String> getNames(String sentence) {

        // remove invalid unicode characters
        sentence = sentence.replaceAll("[^\\u0009\\u000a\\u000d\\u0020-\\uD7FF\\uE000-\\uFFFD]", "");
        // remove other unicode characters coreNLP can't handle
        sentence = sentence.replaceAll("[\\uD83D\\uFFFD\\uFE0F\\u203C\\u3010\\u3011\\u300A\\u166D\\u200C\\u202A\\u202C\\u2049\\u20E3\\u300B\\u300C\\u3030\\u065F\\u0099\\u0F3A\\u0F3B\\uF610\\uFFFC]", "");


        List<String> nameList = new ArrayList<String>();
        tokens = (tokenizerFactory.getTokenizer(new StringReader(sentence.trim()))).tokenize();
//        System.out.println("***" + sentence + "***");
        tree = parser.apply(tokens);
        leaves = tree.getLeaves();

        for (Tree leaf : leaves) {
            if (leaf.label().value().equals("v.")) {
                Tree p = leaf.parent(tree).parent(tree);
                if (p.getLeaves().size() < 3) {
                    p = p.parent(tree);
                }
                List<Tree> nameLeaves = p.getLeaves();

                StringBuilder sb = new StringBuilder();
                for (Tree nleaf : nameLeaves) {
                    String tag = nleaf.parent(tree).value();
                    if (tag.equals("NNP") || tag.equals("NNPS") || tag.equals("CC")) {
                        sb.append(nleaf.label().value());
                        sb.append(" ");
//                        System.out.println(nleaf.label().value() + "-" + nleaf.parent(tree).value());
                    }
                }
                String extName = sb.toString();
                int nameSize = extName.length();
                if (nameSize > 3) {
                    extName = extName.substring(0, (nameSize - 1));
                    nameList.add(extName);
                    System.out.println("===" + extName);
                }
            }
        }
        return nameList;
    }

    public static void main(String[] args) {
        test4 en = new test4();
        en.getNames("Buddhi v. Kasun The defender of legislation that differentiates on the basis of gender v. must show at least that the classification serves important governmental objectives");


    }
}
