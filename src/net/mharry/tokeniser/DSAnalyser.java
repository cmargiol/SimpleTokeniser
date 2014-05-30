package net.mharry.tokeniser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

public class DSAnalyser {
    
    Tokeniser t = new Tokeniser();
    
    public void analyse() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuffer sb = new StringBuffer();
        String line = "";
        
        System.out.println("Please provide your input. Empty line will signify end of input:");
        try {
            while ((line = br.readLine()) != null && line.length() > 0) {
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println("IOException when reading from stdin: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
        printMap(t.lex(sb.toString()));
    }
    
    static private String arrayEntryAsString(ArrayList<Integer> l) {
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

    static public void printMap(SortedMap<String, ArrayList<Integer>> map) {
        System.out.println("-- List of tokens --");
        for (Map.Entry<String, ArrayList<Integer>> entry : map.entrySet()) {
            System.out.println(String.format("%-25s %s",
                    entry.getKey(), 
                    arrayEntryAsString(entry.getValue())));
        }
    }

    public static void main(String[] args) {
        DSAnalyser da = new DSAnalyser();
        da.analyse();
    }
}
