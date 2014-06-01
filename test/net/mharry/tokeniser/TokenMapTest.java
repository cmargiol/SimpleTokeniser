package net.mharry.tokeniser;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class TokenMapTest {

    TokenMap t;
    @Before
    public void setUp() throws Exception {
        t = new TokenMap();
    }

    @Test
    public void testToString() {
        String expString = "test                      {1:0}\ntest2                     {2:0,2}\n";
        t.put("test", 0);
        t.put("test2", 0);
        t.put("test2", 2);
        System.out.println(t.toString());
        assertTrue(expString.equals(t.toString()));
    }

    @Test
    public void testPutNoEntry() {
        t.put("test", 0);
        assertTrue(Arrays.equals(t.get("test").toArray(), new Integer[]{0}));
    }

    @Test
    public void testPutEntry() {
        t.put("test", 0);
        t.put("test", 0);
        assertTrue(Arrays.equals(t.get("test").toArray(), new Integer[]{0,0}));
    }

    @Test
    public void testLowerCase() {
        t.put("FEyNmAN", 0);
        assertTrue(t.containsKey("feynman"));
        assertFalse(t.containsKey("FEyNmAN"));
    }
    
    @Test 
    public void testOrder() {
        t.put("bazinga", 0);
        t.put("a", 0);
        t.put("b", 0);
        t.put("f", 0);
        t.put("w", 0);
        String prevKey = t.firstKey();
        assertTrue(t.firstKey().equals("a"));
        for (String currentKey : t.keySet()){            
            assertTrue(currentKey.compareTo(prevKey) >= 0);
            prevKey = currentKey;
        }
    }
}
