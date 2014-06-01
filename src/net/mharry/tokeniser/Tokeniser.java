package net.mharry.tokeniser;

public class Tokeniser {

    private final String SENTENCE_DELIMS = "\\.?!";
    private final String DELIMS = ",;()\\[\\]\"{}:“”";
    
    /**
     * Takes a string and returns a concordance. Each word has to contain
     * at least one letter, cannot start with a "." and cannot contain the
     * following characters: <code>?!,;()[]{}"</code> 
     */
    public TokenMap lex(String text) {
        TokenMap tokens = new TokenMap();
        int tokensB4CurSentence = 0;
        int sentenceCount = 0;

        for (int loc=0; loc < text.length(); loc++) {
            char c = text.charAt(loc);
            if (Character.isWhitespace(c)) {
                continue;
            } else if (SENTENCE_DELIMS.contains(Character.toString(c))){
                if (tokensB4CurSentence < tokens.size()) {
                    // only count a sentence if the previous one had any tokens
                    sentenceCount++;
                    tokensB4CurSentence = tokens.size();
                }
            } else {
                loc = addNextToken(text, loc, sentenceCount, tokens);
            }
        }
        return tokens;
    }
    
    /**
     * Gets the next token starting from the given location and stores it in the passed TokenMap
     * (for the given sentence). In order for a sequence of characters to qualify as a token, 
     * they need to contain at least one letter. Otherwise, they are ignored (e.g. " - " is 
     * ignored but "c++" is a valid token).
     * 
     * <p/>Rules are simple: If a char is anything but a delimiter, then it's part of the token
     * <br/> One exception: if it's a dot, then it's part of the token, unless the next character 
     * is whitespace, end of stream, or another word.
     * 
     * @return The location of the last character dealt with
     */
     int addNextToken(String text, int loc, int curSentence, TokenMap tokens) {
        StringBuffer sb = new StringBuffer();
        boolean hasLetter = false;

        for ( ; loc < text.length(); loc++) {
            char c = text.charAt(loc);
             if (c == '.') { //special treatment to cover for tokens like "e.g."
                 if (loc == text.length()-1 
                         || Character.isWhitespace(text.charAt(loc+1)) 
                         || text.charAt(loc+1)== '.') {
                     loc--; // we don't want to deal with sentence delimiters
                     break;
                 }
             } else if (Character.isWhitespace(c) || (SENTENCE_DELIMS).contains(Character.toString(c))) {
                 loc--;
                 break;
             } else if (DELIMS.contains(Character.toString(c))){
                 break;
             }

             if (Character.isLetter(c)) hasLetter = true;
             sb.append(c);
        }
        if (sb.length() > 0 && hasLetter) {
            tokens.put(sb.toString(), curSentence);
        }
        return loc;
    }
}
