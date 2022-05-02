/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it131_group6.crud_app;

/**
 *
 * @author hp
 */

// Singleton Class
public class DatabaseService {
    
    private final String USERNAME = "root";
    private final String PASSWORD = "Gv4Kr3fSBv61Pqiru3W0r4";
    private final String DATABASENAME = "crud_app";
    private final String LINK = String.format("jdbc:mysql://localhost/%s?useTimezone=true&serverTimezone=UTC", DATABASENAME);
    private final String CONNECTOR = "com.mysql.cj.jdbc.Driver";
    
    
    private DatabaseService s_instance = null;
    private DatabaseService() {}
    
    public DatabaseService getService() {
        if (s_instance == null)
            s_instance = new DatabaseService();
        return s_instance;
    }
    
    private void AddItemToTable(Object Data, String Table) {
        try
        {
            switch(Table)
            {
                default:
                    break;
            }
        }
        catch(Exception e)
        {
            //
        }
    }
}
