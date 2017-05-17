/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TxtFormat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author Kasper
 */
public class Format {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileIO fio = new FileIO();
        
//        fio.createFormattedTxtAuthor(fio.read("D:\\school\\formatted\\allformatted.txt"), "D:\\school\\formatted\\formattedauthors.txt");
//        fio.createFormattedTxtBook(fio.read("D:\\school\\formatted\\allformatted.txt"), fio.read("D:\\school\\formatted\\formattedauthors.txt"), "D:\\school\\formatted\\formattedbooks.txt");
        fio.createFormattedTxtBookCity(fio.read("D:\\school\\formatted\\allformatted.txt"), fio.read("D:\\school\\formatted\\cities.txt"), fio.read("D:\\school\\formatted\\formattedbooks.txt"), "D:\\school\\formatted\\book_city.txt");
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
