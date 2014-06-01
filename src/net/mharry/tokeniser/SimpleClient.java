package net.mharry.tokeniser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleClient {
    
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
        System.out.println(t.lex(sb.toString()));
    }
    

    public static void main(String[] args) {
        SimpleClient sc = new SimpleClient();
        sc.analyse();
    }
}
