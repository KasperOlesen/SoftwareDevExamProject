/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TxtFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kasper
 */
public class init implements Runnable {


    String[] cities;
    String readPath;
    String writePath;

    public init(String[] cities, String readPath) {

        this.cities = cities;
        this.readPath = "D:\\school\\" + readPath;
        this.writePath = readPath + "d";
    }

    public void initialize() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        File folder = new File(readPath);
        File[] listOfFiles = folder.listFiles();
        FileIO fio = new FileIO();
        for (int i = 0; i < listOfFiles.length; i++) {
            File file = listOfFiles[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                System.out.println("Working: " + readPath + "\\" + file.getName());
                fio.createFormattedTxt(fio.read((readPath + "\\" + file.getName())), cities, ("D:\\school\\formatted\\" + writePath + ".txt"));
            }
        }
    }

    @Override
    public void run() {
        try {
            initialize();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(init.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
