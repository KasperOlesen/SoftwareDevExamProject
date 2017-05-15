/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TxtFormat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kasper
 */
public class FileIO {

    public BufferedReader read(String path, String encoding) throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream, encoding));
        return br;
    }

    public BufferedReader read(String path) throws FileNotFoundException, UnsupportedEncodingException {
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        return br;
    }

    public void writeCities(BufferedReader br, String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (PrintWriter writer = new PrintWriter(path, "UTF-8")) {
            String strLine;

            while ((strLine = br.readLine()) != null) {
                String[] values = strLine.split("\\t", -1);
                if (values.length > 4) {
                    writer.println(values[0] + "," + values[1] + "," + values[4] + "," + values[5]);
                }
            }
        }
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(Format.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createFormattedTxt(BufferedReader br, String[] cities, String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            String strLine;
            boolean firstCity = true;
            boolean bookStarted = false;
            String bookTitle = "NO TITLE";
            String bookAuthor = "NO AUTHOR";
            ArrayList<String> cityList = new ArrayList();

            while ((strLine = br.readLine()) != null) {
                if (!bookStarted) {
                    if (strLine.contains("Title: ")) {
                        String[] tLine = strLine.split(":");
                        if (tLine.length > 1) {
                            bookTitle = tLine[1].substring(1);
                        }
                    } else if (strLine.contains("Author: ")) {
                        String[] tLine = strLine.split(":");
                        if (tLine.length > 1) {
                            bookAuthor = tLine[1].substring(1);
                        }
                    } else if (strLine.contains("***") && strLine.contains("GUTENBERG")) {
                        writer.append(bookTitle + "#" + bookAuthor);
                        bookStarted = true;
                    }
                } else {

                    String[] tLine = strLine.split("\\s+");

                    for (int k = 0; k < cities.length; k++) {
                        for (int l = 0; l < tLine.length; l++) {
                            if (tLine[l].equals(cities[k]) && !cityList.contains(cities[k])) {
                                cityList.add(cities[k]);
                                if (!firstCity) {
                                    writer.append("," + cities[k]);
                                } else if (firstCity) {
                                    writer.append("#" + cities[k]);
                                    firstCity = false;
                                }
                            }

                        }

                    }

                }

            }
//            writer.append(bookTitle + "#" + bookAuthor);
//            for (int i = 0; i < cityList.size(); i++) {
//                if (!firstCity) {
//                    writer.append("," + cityList.get(i));
//                } else if (firstCity) {
//                    writer.append("#" + cityList.get(i));
//                    firstCity = false;
//                }
//            }
            if (bookStarted && cityList.isEmpty()) {
                writer.append("#NO CITIES");
            }
            writer.newLine();

        }
        try {
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(Format.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] getCities(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileIO fio = new FileIO();
        BufferedReader citiesBr = fio.read(path);
        String[] cities = new String[23673];
        String strLine2;
        int i = 0;
        while ((strLine2 = citiesBr.readLine()) != null) {
//                System.out.println(strLine2.split(",")[1]);
            cities[i] = strLine2.split(",")[1];
            i++;
        }
        return cities;
    }
}
