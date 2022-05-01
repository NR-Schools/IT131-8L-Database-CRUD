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
    
    private DatabaseService s_instance = null;
    private DatabaseService() {}
    
    public DatabaseService getService() {
        if (s_instance == null)
            s_instance = new DatabaseService();
        return s_instance;
    }
}
