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

    public void createFormattedTxtAuthor(BufferedReader br, String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            String strLine;
            int count = 0;
            ArrayList<String> authorList = new ArrayList();

            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("#")) {
                    String[] aLine = strLine.split("#");
                    if (aLine.length > 1 && !authorList.contains(aLine[1])) {
                        writer.append(count + "#" + aLine[1]);
                        count++;
                        authorList.add(aLine[1]);
                        writer.newLine();
                    }

                }

            }
            try {
                br.close();

            } catch (IOException ex) {
                Logger.getLogger(Format.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createFormattedTxtBook(BufferedReader br, BufferedReader au, String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            String strLine;
            String auLine;
            ArrayList<String> bookList = new ArrayList();
            ArrayList<String> authorList = new ArrayList();
            while ((auLine = au.readLine()) != null) {
                if (auLine.contains("#")) {
                    String[] aLine = auLine.split("#");
                    if (aLine.length > 1) {
                        authorList.add(aLine[0] + "#" + aLine[1]);
                    }

                }
            }

            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("#")) {
                    String[] aLine = strLine.split("#");
                    if (aLine.length > 1) {
                        bookList.add(aLine[0] + "#" + aLine[1]);
                    }

                }

            }

            for (int i = 0; i < bookList.size(); i++) {
                for (int j = 0; j < authorList.size(); j++) {
                    if (bookList.get(i).split("#")[1].equals(authorList.get(j).split("#")[1])) {
                        writer.append(i + "#" + authorList.get(j).split("#")[0] + "#" + bookList.get(i).split("#")[0]);
                        writer.newLine();
                    }
                }
            }
            try {
                br.close();

            } catch (IOException ex) {
                Logger.getLogger(Format.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createFormattedTxtBookCity(BufferedReader br, BufferedReader ci, BufferedReader brId, String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {

            String strLine;
            String strIdLine;
            String ciLine;
            ArrayList<String> bookList = new ArrayList();
            ArrayList<String> bookIdList = new ArrayList();
            ArrayList<String> cityList = new ArrayList();
            ArrayList<String> book2List = new ArrayList();

            while ((ciLine = ci.readLine()) != null) {

                String[] cLine = ciLine.split(",");
                if (cLine.length > 1) {
                    cityList.add(cLine[0] + "#" + cLine[1]);
                }

            }
            while ((strIdLine = brId.readLine()) != null) {

                String[] brIdLine = strIdLine.split("#");
                if (brIdLine.length > 1) {
                    bookIdList.add(brIdLine[0] + "#" + brIdLine[2]);
                }

            }

            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("#")) {
                    String[] aLine = strLine.split("#");
                    if (aLine.length > 1) {
                        bookList.add(aLine[0] + "#" + aLine[2]);
                    }

                }

            }
            //create table with bookId, book name and cities by joining arrays into a new one
            for (int i = 0; i < bookList.size(); i++) {
                for (int j = 0; j < bookIdList.size(); j++) {
                    if (bookList.get(i).split("#")[0].equals(bookIdList.get(j).split("#")[1])) {
//                        System.out.println(bookIdList.get(j).split("#")[0] + "#" + bookList.get(i).split("#")[0] + "#" + bookList.get(i).split("#")[1]);
                        book2List.add(bookIdList.get(j).split("#")[0] + "#" + bookList.get(i).split("#")[0] + "#" + bookList.get(i).split("#")[1]);
                    }
                }
            }

            //
            for (int i = 0; i < book2List.size(); i++) {
                String[] cityArr = book2List.get(i).split("#")[2].split(",");
                for (int j = 0; j < cityArr.length; j++) {
                    for (int k = 0; k < cityList.size(); k++) {
                        if (cityArr[j].equals(cityList.get(k).split("#")[1])) {
                            System.out.println("write");
                            writer.append(book2List.get(i).split("#")[0] + "#" + cityList.get(k).split("#")[0]);
                            writer.newLine();
                        }
                    }

                }
            }
            try {
                br.close();

            } catch (IOException ex) {
                Logger.getLogger(Format.class.getName()).log(Level.SEVERE, null, ex);
            }
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
