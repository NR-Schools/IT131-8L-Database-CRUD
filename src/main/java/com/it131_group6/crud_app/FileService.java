/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it131_group6.crud_app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hp
 */

public class FileService {
    
    private final String DB_CONFIG_FILE = "sql_config.txt";
    
    public FileService() {
        //
    }
    
    public void InitializeEnv() {
        try {
            // Create SQL File to Pull Username and Password
            File nFile = new File(DB_CONFIG_FILE);

            if(!nFile.exists()) {
                // Create File
                nFile.createNewFile();
                
                // Write To File
                BufferedWriter writeFile = new BufferedWriter(new FileWriter(nFile));
                
                // Populate with some db configs :: IMPROVEMENT (Can be stored using json)
                writeFile.write("root");
                writeFile.newLine();
                
                writeFile.write("tTyAnp8wX73CscU3");
                writeFile.newLine();
                
                // Close Buffer
                writeFile.close();
            }
        }
        catch(Exception ex) {
            Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DataModel.ConfigData getDatabaseConfig() {
        try {
            DataModel.ConfigData configData = new DataModel(). new ConfigData();
            
            File configFile = new File(DB_CONFIG_FILE);
            BufferedReader readConfig = new BufferedReader(new FileReader(configFile));
            
            configData.Username = readConfig.readLine();
            configData.Password = readConfig.readLine();
            
            readConfig.close();
            return configData;
        }
        catch(FileNotFoundException ex) {
            Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(FileService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
