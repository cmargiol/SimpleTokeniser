package net.mharry.tokeniser;

import java.util.ArrayList;
import java.util.SortedMap;

public class EarlyTokeniser {
    
    /**
     * This method does the job in a simpler way. If you find whitespace then you're up for a 
     * token. But you need to go back and trim it (i.e. "Finally!!!" will become "Finally")
     * 
     * Almost works, but there is a problem with it: It doesn't count sentences. Can be done, 
     * but it would be cumbersome. Also, doesn't match the spec in that "e.g." is trimmed
     * to "e.g". Plus not super efficient as it has to pass through some chars twice.
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
