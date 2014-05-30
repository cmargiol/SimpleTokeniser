package net.mharry.tokeniser;

import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


public class Tokeniser {

    private final String SENTENCE_DELIMS = "?!";
    private final String DELIMS = ",;()\\[\\]\"{}:“”";
    
    public SortedMap<String, ArrayList<Integer>> lex(String text) {
        
        SortedMap<String, ArrayList<Integer>> tokenMap = new TreeMap<String, ArrayList<Integer>>();
        StringBuffer sb = new StringBuffer();
        int sentenceCount = 0;        

        // for more complex analysis, keeping states might be needed. 
        // We should be able to do the trick just with the next and prev chars
        char prevChar = '\0', nextChar = '\0', c = '\0';
        boolean hadTokens = false;
        
        for (int i=0; i < text.length(); i++) {
            c = text.charAt(i);
            if (i!=text.length()-1) // not last char
                nextChar = text.charAt(i+1);
            if (c == '.') {
                if (prevChar == '.')
                    continue;
                else if (Character.isWhitespace(nextChar) || nextChar == '.') {
                    // A '.' is part of a word unless before whitespace or another dot
                    if (addToMap(sb, sentenceCount, tokenMap)) {
                        hadTokens = true;
                        sb = new StringBuffer();
                    }
                    if (hadTokens) {
                        sentenceCount++;
                        hadTokens = false;
                    }
                    i++;
                }
                else
                    sb.append(c); // "e.g."
            } else if (SENTENCE_DELIMS.contains(Character.toString(c))) {
                if (SENTENCE_DELIMS.contains(Character.toString(prevChar)))
                    continue;
                if (addToMap(sb, sentenceCount, tokenMap)) {
                    hadTokens = true;
                    sb = new StringBuffer();
                }
                if (hadTokens) {
                    sentenceCount++;
                    hadTokens = false;
                }
            } else if (Character.isWhitespace(c) || (DELIMS).contains(Character.toString(c))) {
                if (addToMap(sb, sentenceCount, tokenMap)) {
                    hadTokens = true;
                    sb = new StringBuffer();
                }
            } else if (!Character.isLetter(c) && sb.length() == 0 && Character.isWhitespace(nextChar)) {
            	//each token must have at least one letter
                continue;
            } else {
                sb.append(c);
            }
            prevChar =c;
        }

        addToMap(sb, sentenceCount, tokenMap); //any leftovers in the buffer
        return tokenMap;
    }

    public boolean addToMap(StringBuffer sb, int sentence, Map<String, ArrayList<Integer>> tokenMap){
        if (null != sb && sb.length() > 0) {
            String key = sb.toString().toLowerCase();
            if (tokenMap.containsKey(key))
                tokenMap.get(key).add(sentence);
            else {
                ArrayList<Integer> l = new ArrayList<Integer>();
                l.add(sentence);
                tokenMap.put(key, l);
            }
            return true;
        }
        return false;
    }

    //============================earlier attempt below======================================
    
    /**
     * This method does the job in a simpler way. If you find whitespace then you're up for a 
     * token. But you need to go back and trim it (i.e. "Finally!!!" will become "Finally")
     * 
     * Almost works, but there is a problem with it: It doesn't count sentences. Can be done, 
     * but it would be cumbersome. Also, doesn't match the spec in that "e.g." is trimmed
     * to "e.g"
     */
    public SortedMap<String, ArrayList<Integer>> lexSimple(String text) {
        
        TokenMap tokens = new TokenMap();
        StringBuffer sb = new StringBuffer();
        int sentenceCount = 0;

        for (int i=0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            if (Character.isWhitespace(c) || i==text.length()-1) {
                String token = trimToken(sb.toString().toLowerCase());
                sb = new StringBuffer();
                if (token.length() > 0) 
                    tokens.put(token, sentenceCount);
            } else {
                sb.append(c);
            }
        }
        if (sb.length() > 0) //any leftovers in the buffer
            tokens.put(sb.toString().toLowerCase(), sentenceCount);
        return tokens;
        
    }

    /**
     * Convenience class to make put calls to the particular type of map we want a tiny bit smaller.
     * It takes the token and the sentence number and appends to the "appearances list" if there is one, or
     * creates one if there isn't. Could also be a wrapper method.
     */
    public class TokenMap extends TreeMap<String, ArrayList<Integer>> {
        
        private static final long serialVersionUID = 1L;

        public boolean put(StringBuffer sb, int sentence) {
            if (null != sb && sb.length()>0) {
                put(sb.toString(), sentence);
                return true;
            }
            return false;
        }
        
        public void put(String key, int sentence) {
            key = key.toLowerCase();
            if (containsKey(key)) {
                get(key).add(sentence);
            } else {
                ArrayList<Integer> l = new ArrayList<Integer>();
                l.add(sentence);
                put(key, l);
            }
        }
    }

    protected String trimToken (String token) {
        String redundant = "[\"\\-!(),\\[\\]{}.?']";
        if (null == token || token.isEmpty())
            return "";
        if (token.matches(".*"+redundant))
            return trimToken(token.substring(0, token.length()-1));
        else if (token.matches(redundant+".*"))
            return trimToken(token.substring(1));
        else 
            return token;
    }
}
