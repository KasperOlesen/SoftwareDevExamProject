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
        String[] cities = fio.getCities("D:\\school\\cities.txt");

        Thread t1 = new Thread(new init(cities, "1"));
        Thread t2 = new Thread(new init(cities, "2"));
        Thread t3 = new Thread(new init(cities, "3"));
        Thread t4 = new Thread(new init(cities, "4"));
        t1.start();
        t2.start();
        t3.start();
        t4.start();

    }

}
