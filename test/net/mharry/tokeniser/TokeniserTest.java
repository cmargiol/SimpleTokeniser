package net.mharry.tokeniser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

public class TokeniserTest {

    Tokeniser t = new Tokeniser();
    SortedMap<String, ArrayList<Integer>> res;
    
    @Test
    public void testSimple() {
        res = t.lex("this is a test");        
        assertTrue(res.size()==4);
    }

    @Test
    public void testEndsWithDot() {
        res = t.lex("this is a test.");
        assertTrue(res.size()==4);
    }
    
    @Test
    public void testMoreSentences() {
        res = t.lex("One two three apples. Sorry four apples - yes apples");
        assertTrue(res.size()==7);
        assertTrue(Arrays.equals(res.get("apples").toArray(), new Integer[]{0,1,1}));        
    }
    
    @Test
    public void testThreeDots() {
        res = t.lex("What is the answer to life, universe and everything? Hmmm ...");
        assertTrue(res.size()==10);
        assertTrue(res.get(".")==null);
    }

    @Test
    public void testThreeDotsInTheMiddle() {
        res = t.lex("A self driving car you say... I hear you...");
        assertTrue(res.size()==8);
        assertTrue(res.get(".")==null);
    }
    
    @Test
    public void testEG () {
        res = t.lex("here's a tricky one for you e.g.,");
        assertTrue(res.get("e.g.").size()==1);
    }
    
    @Test
    public void testApostrophe() {
        res = t.lex("That's it!!! That's it!!!");
        assertTrue(res.size()==2);
        assertTrue(res.get("that's").size()==2);
    }
    
    @Test 
    public void testTweet() {
        String text = "Friday today - how about this song? #fridaysong";
        res = t.lex(text);
        assertTrue(res.containsKey("#fridaysong"));
    }
    
    @Test 
    public void testTrimmer() {
        String token = "!!!-finally,[]{}.!()?!";
        assertTrue(t.trimToken(token).equals("finally"));
    }

    @Test
    public void testSentenceCount() {
        res = t.lex("Two sentences!!! Not 4");
        assertTrue(Arrays.equals(res.get("not").toArray(), new Integer[]{1}));
        res = t.lex("Two sentences... Not 4");
        assertTrue(Arrays.equals(res.get("not").toArray(), new Integer[]{1}));
    }

    @Test
    public void testRandomSpacesCount() {
        res = t.lex("Two sentences! ! ! Yes, two");
        assertTrue(Arrays.equals(res.get("two").toArray(), new Integer[]{0,1}));
        res = t.lex("Two sentences. . . Yes, two");
        assertTrue(Arrays.equals(res.get("two").toArray(), new Integer[]{0,1}));
    }

    @Test
    public void testLonelyHyphen() {
        res = t.lex("this is a hyphen - don't count it as a word");
        assertFalse(res.containsKey("-"));
    }

    @Test
    public void testHyphenInWord() {
        res = t.lex("This is another hyphen-in-word");
        assertTrue(res.containsKey("hyphen-in-word"));
    }

    @Test
    public void testSentenceEnd() {
        res = t.lex("This is the end ! The end, my friend");
        assertTrue(Arrays.equals(res.get("end").toArray(), new Integer[]{0,1}));
    }

    @Test
    public void testSentenceEndDot() {
        res = t.lex("This is the end . The end");
        assertTrue(Arrays.equals(res.get("end").toArray(), new Integer[]{0,1}));
    }
    
    @Test
    public void testDSParagraph() {
        String tex1 = "We’d like you to demonstrate how a tokenization algorithm could be used to create a “concordance”. "
                + "A concordance is a bit like an index: It’s an alphabetical list of all the words present in a body of text, complete with citations "
                + "of where each word appears (e.g., the line, page or sentence number). Using the programming language of your choice - ideally Scala, "
                + "Java or Go as they are widely-used at DataSift - write a program that will read a document from stdin and generate a concordance that "
                + "is printed to stdout. The printed format should at least consist of a sorted list of citations with the frequency and sequence of "
                + "zero-indexed sentence numbers where that word occurs.";
        res = t.lex(tex1);
        assertTrue(Arrays.equals(res.get("a").toArray(), new Integer[]{0,0,1,1,1,2,2,2,3}));
        assertTrue(Arrays.equals(res.get("concordance").toArray(), new Integer[]{0,1,2}));
        assertTrue(Arrays.equals(res.get("widely-used").toArray(), new Integer[]{2}));
        assertTrue(Arrays.equals(res.get("concordance").toArray(), new Integer[]{0,1,2}));
        assertTrue(Arrays.equals(res.get("we’d").toArray(), new Integer[]{0}));
        DSAnalyser.printMap(res);
    }

    @Test 
    public void testOrder() {
        String text = "a b c e f";
        res = t.lex(text);
        String prevKey = res.firstKey();
        for (String currentKey : res.keySet()){            
            assertTrue(currentKey.compareTo(prevKey) >= 0);
            prevKey = currentKey;
        }
    }
}
