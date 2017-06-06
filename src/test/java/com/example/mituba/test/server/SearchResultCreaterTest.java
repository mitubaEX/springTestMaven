package com.example.mituba.test.server;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mituba on 2017/06/06.
 */
public class SearchResultCreaterTest {
    @Test
    public void getSearchResultOfSearchResultTest(){
        assertTrue(new SearchResultCreater().getSearchResultOfSearchResult("").isEmpty());
        assertEquals(new SearchResultCreater().getSearchResultOfSearchResult("a,b,0.2\nc,d,0.4").size(), 2);

        assertTrue(new SearchResultCreater().getSearchResultOfSearchResult(new ArrayList<>()).isEmpty());
        String[] strArray = {"a,b,0.2", "c,d,0.4"};
        assertEquals(new SearchResultCreater().getSearchResultOfSearchResult(Arrays.asList(strArray)).size(), 2);

        try {
            assertEquals(new SearchResultCreater().readFile(
                    new BufferedReader(new FileReader(new File("src/test/resources/FizzBuzz.class.csv"))), "100").size(), 1
            );
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
