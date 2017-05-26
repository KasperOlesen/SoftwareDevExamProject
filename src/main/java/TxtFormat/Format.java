/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TxtFormat;

import Migration.AuthorMigrateNeo;
import Migration.BookMigrateNeoOpti;
import Migration.CitiesMigrateNeo;
import Migration.BooksMigrateNeo;
import Migration.CreateRelationshipsNeo;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.neo4j.driver.v1.*;

/**
 *
 * @author Kasper
 */
public class Format {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        //run below to create formatted files
        
//        FileIO fio = new FileIO();

//        fio.createFormattedTxtAuthor(fio.read("D:\\school\\formatted\\allformatted.txt"), "D:\\school\\formatted\\formattedauthors.txt");
//        fio.createFormattedTxtBook(fio.read("D:\\school\\formatted\\allformatted.txt"), fio.read("D:\\school\\formatted\\formattedauthors.txt"), "D:\\school\\formatted\\formattedbooks.txt");
//        fio.createFormattedTxtBookCity(fio.read("data/allformatted.txt"), fio.read("data/cities.txt"), fio.read("data/formattedbooks.txt"), "data/book_city.txt");

//Run to import data to neo4j

        CitiesMigrateNeo cmn = new CitiesMigrateNeo(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "class"));
        cmn.performMigration("/data/cities.csv");

        AuthorMigrateNeo amn = new AuthorMigrateNeo("bolt://localhost:7687",
                AuthTokens.basic("neo4j", "class"));
        amn.performMigration("/data/formattedauthors.txt");

        BooksMigrateNeo bmn = new BooksMigrateNeo(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "class"));
        bmn.performMigration("/data/formattedbooks.txt");
//
//        //Laver relations mellem book og city fra book_city.txt, tager laaang tid
        CreateRelationshipsNeo crn = new CreateRelationshipsNeo(
                "bolt://localhost:7687",
                AuthTokens.basic("neo4j", "class"));
        crn.performMigration("/data/book_city.txt");

        // BookMigrateNeoOpti bmno = new BookMigrateNeoOpti(
        //              "bolt://localhost:7687",
        //            AuthTokens.basic("neo4j", "class"));
        //  bmno.performMigration("/data/allformatted2.txt");
        
        //Below code was used to start the operation of scraping all the data we needed from the books 
        
//        String[] cities = fio.getCities("D:\\school\\cities.txt");
//        Thread t1 = new Thread(new init(cities, "1"));
//        Thread t2 = new Thread(new init(cities, "2"));
//        Thread t3 = new Thread(new init(cities, "3"));
//        Thread t4 = new Thread(new init(cities, "4"));
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
    }

}
