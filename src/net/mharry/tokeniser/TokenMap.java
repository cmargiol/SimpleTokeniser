package net.mharry.tokeniser;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Convenience class to add a special put() method and a toString that prints according to the 
 * spec.
 */
public class TokenMap extends TreeMap<String, ArrayList<Integer>> {
    
    private static final long serialVersionUID = 1L;

    /**
     * It takes the token and the sentence number and appends to the "appearances list" 
     * if there is one, or creates one if there isn't
     */
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

    private String arrayEntryAsString(ArrayList<Integer> l) {
        StringBuffer sb = new StringBuffer();
        int len = l.size();
        if (len < 1)
            return "{}";
        sb.append("{"+ len + ":" + l.get(0));
        for (int i = 1; i < len; i++) {
            sb.append("," + l.get(i));
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, ArrayList<Integer>> entry : entrySet()) {
            sb.append(String.format("%-25s %s\n",
                    entry.getKey(), 
                    arrayEntryAsString(entry.getValue())));
        }
        return sb.toString();
    }
}