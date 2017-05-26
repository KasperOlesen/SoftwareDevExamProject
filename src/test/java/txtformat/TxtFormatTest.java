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

    @Test
    public void getCitiesContentTest() throws UnsupportedEncodingException, IOException {
        FileIO fio = new FileIO();
        String[] test = {"Wichita", "Ashland", "Bowling Green", "Burlington", "Covington", "Danville", "Elizabethtown", "Erlanger", "Fern Creek"};
        String[] cities = fio.getCities("data/test-data/cities.csv");

        assertThat(cities, is(test));
    }

    @Test
    public void createFormattedAuthorsTest() throws UnsupportedEncodingException, IOException {
        FileIO fio = new FileIO();
        String[] test = {"0#author1", "1#author2", "2#author3", "3#author4"};
        fio.createFormattedTxtAuthor(fio.read("data/test-data/allformatted.txt"), "data/test-data/formattedauthors.txt");
        BufferedReader br = fio.read("data/test-data/formattedauthors.txt");
        String[] authors = new String[test.length];
        int i = 0;
        String strLine;
        while ((strLine = br.readLine()) != null) {
            authors[i] = strLine;
            i++;
        }
        assertThat(authors, is(test));
    }

    @Test
    public void createFormattedTxtBookTest() throws UnsupportedEncodingException, IOException {
        FileIO fio = new FileIO();
        String[] test = {"0book1", "1book2", "2book3", "3book4"};
        fio.createFormattedTxtBook(fio.read("data/test-data/allformatted.txt"), fio.read("data/test-data/formattedauthors.txt"), "data/test-data/formattedbooks.txt");
        BufferedReader br = fio.read("data/test-data/formattedbooks.txt");
        String[] books = new String[test.length];
        int i = 0;
        String strLine;
        while ((strLine = br.readLine()) != null) {
            books[i] = strLine.split("#")[1] + strLine.split("#")[2];
            i++;
        }
        assertThat(books, is(test));
    }
//     @Test
//     public void createFormattedTxtBookCityTest() throws UnsupportedEncodingException, IOException {
//     FileIO fio = new FileIO();
//     String[] test = {"0book1", "1book2", "2book3", "3book4"};
//     fio.createFormattedTxtBook(fio.read("data/test-data/allformatted.txt"), fio.read("data/test-data/formattedauthors.txt"), "data/test-data/formattedbooks.txt");
//     BufferedReader br = fio.read("data/test-data/formattedbooks.txt");
//     String[] books = new String[test.length];
//     int i = 0;
//     String strLine;
//     while((strLine = br.readLine()) != null){
//         books[i] = strLine.split("#")[1] + strLine.split("#")[2];
//         i++;
//     }
//     assertThat(books, is(test));
//     }

     @Test
     public void writeFormattedTxtTest() throws UnsupportedEncodingException, IOException {
     FileIO fio = new FileIO();
     String[] cities = fio.getCities("data/test-data/cities.csv");
         for (int i = 0; i < cities.length; i++) {
             System.out.println(cities[i]);
         }
     String path = "data/test-data/allformattedtest.txt";
//     BufferedReader br = Mockito.mock(BufferedReader.class);
     
//     Mockito.when(br.readLine()).thenReturn("aoidfghsohg", "oufuidah", "Title: Test Title", " ", "Author: Test Author", "dfghdfghdfgjr6y", "*** hgdhd GUTENBERG", "AOIHS)DHAHFW Wichita", "iogoushduh Moscow Danville", "Covington ogsuhdfgiyush", "     Erlanger    " , "Fern Creek");
     fio.createFormattedTxt(fio.read("data/test-data/testbook.txt"), cities, path);
     
     BufferedReader br1 = fio.read(path);
     assertThat(br1.readLine(), is("Test Book#Test Author#Wichita,Danville,Covington,Ashland,Erlanger"));
     }
}
