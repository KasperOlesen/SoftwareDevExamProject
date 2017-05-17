/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package txtformat;

import TxtFormat.FileIO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;
import org.mockito.Mockito;


/**
 *
 * @author Kasper
 */
public class TxtFormatTest {
    
    public TxtFormatTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void getCitiesContentTest() throws UnsupportedEncodingException, IOException {
     FileIO fio = new FileIO();
     String[] cities = fio.getCities("docs/test/testdata/Testcities.txt");
     
     assertThat(cities[0], is("London"));
     assertThat(cities[4], is("Copenhagen"));
     }
     
     @Test
     public void writeFormmatedTxtTest() throws UnsupportedEncodingException, IOException {
     FileIO fio = new FileIO();
     String[] cities = fio.getCities("docs/test/testdata/Testcities.txt");
     String path = "docs/test/testdata/testWrite.txt";
     BufferedReader br = Mockito.mock(BufferedReader.class);
     
//     Mockito.when(br.readLine()).thenReturn("aoidfghsohg", "oufuidah", "Title: Test Title", " ", "Author: Test Author", "dfghdfghdfgjr6y", "*** hgdhd GUTENBERG", "AOIHS)DHAHFW London", "iogoushduh Moscow niugsdh", "Paris ogsuhdfgiyush", "     Berlin    " , "Copenhagen");
     fio.createFormattedTxt(fio.read("docs/test/testdata/1.txt"), cities, path);
     
     BufferedReader br1 = fio.read(path);
     assertThat(br1.readLine(), is("Test Book#Test Author#Moscow,London,Copenhagen,Paris,Berlin"));
     }
}
