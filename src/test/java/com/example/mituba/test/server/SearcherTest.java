package com.example.mituba.test.server;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mituba on 2017/06/06.
 */
public class SearcherTest {
    @Test
    public void searchPerformTest(){
        assertTrue(new Searcher("a", "b", "2000", "2-gram", "100").searchPerform().isEmpty());
    }

    @Test
    public void searchPerformNullTest(){
        assertTrue(new Searcher(null, null,  null, null, null).searchPerform().isEmpty());
    }
}
