/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it131_group6.crud_app;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author hp
 */
/*
 * SQLs are not using prepared statements and thus, more vulnerable to SQL injections
 *
 */
public class DatabaseService {
    
    private String USERNAME = "";
    private String PASSWORD = "";
    private final String LINK = "jdbc:mysql://localhost?useTimezone=true&serverTimezone=UTC";
    private final String CONNECTOR = "com.mysql.cj.jdbc.Driver";
    
    public DatabaseService() {}
    
    public void InitializeDatabaseConfig(String Username, String Password) {
        USERNAME = Username;
        PASSWORD = Password;
    }
    
    public void InitializeEnv() {
        try { Class.forName(CONNECTOR); } catch(ClassNotFoundException ex) {}
        
        try
        {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            if(!isDBExists("crud_app")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE DATABASE crud_app;"
                );
            }
            
            Statement UseDBQuery = sql_con.createStatement();
            UseDBQuery.executeUpdate(
                    "USE crud_app;"
            );

            if(!isTableExists("Customer")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Customer ("
                        + "CustNo INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "CustFName VARCHAR(20) NOT NULL,"
                        + "CustLName VARCHAR(20) NOT NULL,"
                        + "CustSex CHAR(1) NOT NULL,"
                        + "CustDOB DATE NOT NULL,"
                        + "CustAddr VARCHAR(255) NOT NULL,"
                        + "CustContactNo VARCHAR(15) NOT NULL,"
                        + "CustEmail VARCHAR(50) NOT NULL,"
                        + "CHECK ( CustSex in ('M', 'F') ))"
                );
            }
            
            if(!isTableExists("Job")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Job ("
                        + "JobCode INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "JobPos VARCHAR(30) NOT NULL,"
                        + "JobDesc VARCHAR(50) NOT NULL,"
                        + "JobSal INTEGER(10) NOT NULL,"
                        + "CHECK (JobSal > 0))"
                );
            }
            
            if(!isTableExists("Employee")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Employee ("
                        + "EmpNo INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "EmpFName VARCHAR(20) NOT NULL,"
                        + "EmpLName VARCHAR(20) NOT NULL,"
                        + "EmpSex char(1) NOT NULL,"
                        + "EmpDOB DATE NOT NULL,"
                        + "EmpAddr VARCHAR(255) NOT NULL,"
                        + "EmpContactNo VARCHAR(15) NOT NULL,"
                        + "EmpEmail VARCHAR(50) NOT NULL,"
                        + "EmpTIN INTEGER(10) NOT NULL,"
                        + "EmpHireDate DATE NOT NULL,"
                        + "EmpJobCode INTEGER(10),"
                        + "CHECK ( EmpSex in ('M', 'F') ),"
                        + "FOREIGN KEY (EmpJobCode) REFERENCES Job(JobCode))"
                );
            }
            
            if(!isTableExists("Contract")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Contract ("
                        + "ContNo INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "ContLockedIn TINYINT(1) NOT NULL,"
                        + "ContWaterDisp TINYINT(1) NOT NULL,"
                        + "ContContainer TINYINT(1) NOT NULL,"
                        + "ContWatAnalysis TINYINT(1) NOT NULL,"
                        + "ContAmtPerMon INTEGER(10) NOT NULL,"
                        + "ContDuration INTEGER(10),"
                        + "ContSDate DATE NOT NULL,"
                        + "ContEDate DATE NOT NULL,"
                        + "ContCustNo INTEGER(10) NOT NULL,"
                        + "FOREIGN KEY (ContCustNo) REFERENCES Customer(CustNo),"
                        + "ContEmpNo INTEGER(10) NOT NULL,"
                        + "FOREIGN KEY (ContEmpNo) REFERENCES Employee(EmpNo))"
                );
            }
            
            if(!isTableExists("Payment")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Payment ("
                        + "PayNo INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "PayDate DATE NOT NULL,"
                        + "PayAmt INTEGER(10) NOT NULL,"
                        + "CHECK (PayAmt > 0))"
                );
            }
            
            // Order_ was used instead of Order since the name has conflicts in MySQL
            if(!isTableExists("Order_")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Order_ ("
                        + "OrdEmpNo INTEGER(10) NOT NULL,"
                        + "OrdNo INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "OrdDeliverDate DATE NOT NULL,"
                        + "OrdDeliverAddr VARCHAR(255) NOT NULL,"
                        + "OrdContNo INTEGER(10) NOT NULL,"
                        + "OrdPayNo INTEGER(10),"
                        + "FOREIGN KEY (OrdEmpNo) REFERENCES Employee(EmpNo),"
                        + "FOREIGN KEY (OrdContNo) REFERENCES Contract(ContNo),"
                        + "FOREIGN KEY (OrdPayNo) REFERENCES Payment(PayNo))"
                );
            }
            
            if(!isTableExists("Supply")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE Supply ("
                        + "SupplyNo INTEGER(10) PRIMARY KEY AUTO_INCREMENT,"
                        + "SupplyName VARCHAR(20) NOT NULL,"
                        + "SupplyQOH INTEGER(10) NOT NULL,"
                        + "SupplyPrice INTEGER(10) NOT NULL,"
                        + "CHECK (SupplyPrice > 0))"
                );
            }
            
            if(!isTableExists("SupplyOrder")) {
                Statement Query = sql_con.createStatement();
                Query.executeUpdate(
                        "CREATE TABLE SupplyOrder ("
                        + "SupplyNo INTEGER(10),"
                        + "OrdNo INTEGER(10),"
                        + "SupplyOrderQty INTEGER(10) NOT NULL,"
                        + "FOREIGN KEY (SupplyNo) REFERENCES Supply(SupplyNo),"
                        + "FOREIGN KEY (OrdNo) REFERENCES Order_(OrdNo),"
                        + "PRIMARY KEY (SupplyNo, OrdNo))"
                );
            }
            
            sql_con.close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void AddItemToTable(Object Data, String Table) {
        try
        {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            String QueryStr = "";
            
            switch(Table)
            {
                case "Customer":
                    DataModel.Customer customer = (DataModel.Customer)Data;
                    QueryStr = String.format("INSERT INTO Customer"
                            + "(CustNo, CustFName, CustLName, CustSex, CustDOB, CustAddr, CustContactNo, CustEmail) "
                            + "VALUES (NULL, \"%s\", \"%s\", \"%c\", \"%s\", \"%s\", \"%s\", \"%s\")",
                            customer.CustFName, customer.CustLName, customer.CustSex, customer.CustDOB, customer.CustAddr, customer.CustContactNo, customer.CustEmail);
                    break;
                case "Employee":
                    DataModel.Employee employee = (DataModel.Employee)Data;
                    QueryStr = String.format("INSERT INTO Employee ("
                            + "EmpNo, EmpFName, EmpLName, EmpSex, EmpDOB, EmpAddr, EmpContactNo, EmpEmail, EmpTIN, EmpHireDate, EmpJobCode) "
                            + "VALUES (NULL, \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"%d\", \"%s\", \"%d\")",
                            employee.EmpFName, employee.EmpLName, employee.EmpSex, employee.EmpDOB, employee.EmpAddr, employee.EmpContactNo, employee.EmpEmail, employee.EmpTIN, employee.EmpHireDate, employee.EmpJobCode);
                    break;
                case "Job":
                    DataModel.Job job = (DataModel.Job)Data;
                    QueryStr = String.format("INSERT INTO Job ("
                            + "JobCode, JobPos, JobDesc, JobSal) "
                            + "VALUES (NULL, \"%s\", \"%s\", \"%d\")",
                            job.JobPos, job.JobDesc, job.JobSal);
                    break;
                case "Contract":
                    DataModel.Contract contract = (DataModel.Contract)Data;
                    if(contract.Duration != null) {
                        QueryStr = String.format("INSERT INTO Contract ("
                            + "ContNo, ContLockedIn, ContWaterDisp, ContContainer, ContWatAnalysis, ContAmtPerMon, ContDuration, ContSDate, ContEDate, ContCustNo, ContEmpNo) "
                            + "VALUES (NULL, %b, %b, %b, %b, \"%d\", \"%d\", \"%s\", \"%s\", \"%d\", \"%d\")",
                            contract.ContLockedIn, contract.ContWaterDisp, contract.ContContainer, contract.ContWatAnalysis, contract.ContAmtPerMon, contract.Duration, contract.ContSDate, contract.ContEDate, contract.ContCustNo, contract.ContEmpNo);
                    }
                    else {
                        QueryStr = String.format("INSERT INTO Contract ("
                            + "ContNo, ContLockedIn, ContWaterDisp, ContContainer, ContWatAnalysis, ContAmtPerMon, ContDuration, ContSDate, ContEDate, ContCustNo, ContEmpNo) "
                            + "VALUES (NULL, %b, %b, %b, %b, \"%d\", NULL, \"%s\", \"%s\", \"%d\", \"%d\")",
                            contract.ContLockedIn, contract.ContWaterDisp, contract.ContContainer, contract.ContWatAnalysis, contract.ContAmtPerMon, contract.ContSDate, contract.ContEDate, contract.ContCustNo, contract.ContEmpNo);
                    }
                    break;
                case "Order_":
                    DataModel.Order order = (DataModel.Order)Data;
                    if(order.OrdPayNo != null) {
                        QueryStr = String.format("INSERT INTO Order_ ("
                            + "OrdEmpNo, OrdNo, OrdDeliverDate, OrdDeliverAddr, OrdContNo, OrdPayNo)"
                            + "VALUES (\"%d\", NULL, \"%s\", \"%s\", \"%d\", \"%d\")",
                            order.OrdEmpNo, order.OrdDeliverDate, order.OrdDeliverAddr, order.OrdContNo, order.OrdPayNo);
                    }
                    else {
                        QueryStr = String.format("INSERT INTO Order_ ("
                            + "OrdEmpNo, OrdNo, OrdDeliverDate, OrdDeliverAddr, OrdContNo, OrdPayNo)"
                            + "VALUES (\"%d\", NULL, \"%s\", \"%s\", \"%d\", NULL)",
                            order.OrdEmpNo, order.OrdDeliverDate, order.OrdDeliverAddr, order.OrdContNo);
                    }
                    break;
                case "Payment":
                    DataModel.Payment payment = (DataModel.Payment)Data;
                    QueryStr = String.format("INSERT INTO Payment ("
                            + "PayNo, PayDate, PayAmt)"
                            + "VALUES (NULL, \"%s\", \"%d\")",
                            payment.PayDate, payment.PayAmt);
                    break;
                case "SupplyOrder":
                    DataModel.SupplyOrder supply_order = (DataModel.SupplyOrder)Data;
                    QueryStr = String.format("INSERT INTO SupplyOrder ("
                            + "SupplyNo, OrdNo, SupplyOrderQty)"
                            + "VALUES (\"%d\", \"%d\", \"%d\")",
                            supply_order.SupplyNo, supply_order.OrdNo, supply_order.SupplyOrderQty);
                    break;
                case "Supply":
                    DataModel.Supply supply = (DataModel.Supply)Data;
                    QueryStr = String.format("INSERT INTO Supply ("
                            + "SupplyNo, SupplyName, SupplyQOH, SupplyPrice)"
                            + "VALUES (NULL, \"%s\", \"%d\", \"%d\")",
                            supply.SupplyName, supply.SupplyQOH, supply.SupplyPrice);
                    break;
            }
            
            Statement SetDB = sql_con.createStatement();
            SetDB.executeUpdate("USE crud_app;");
            
            Statement Query = sql_con.createStatement();
            Query.executeUpdate(QueryStr);
            
            sql_con.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<Object> ReadItemsFromTable(String Table) {
        try
        {
            ArrayList<Object> SQLDatas = new ArrayList<>();
            
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            String QueryStr = "";
            
            Statement SetDB = sql_con.createStatement();
            SetDB.executeUpdate("USE crud_app;");
            
            Statement Query = sql_con.createStatement();
            ResultSet results = Query.executeQuery(String.format("SELECT * FROM %s", Table));
            
            switch(Table) {
                case "Customer":
                    while(results.next()) {
                        DataModel.Customer customer = new DataModel(). new Customer();
                        customer.CustNo = results.getInt("CustNo");
                        customer.CustFName = results.getString("CustFName");
                        customer.CustLName = results.getString("CustLName");
                        customer.CustSex = results.getString("CustSex").charAt(0);
                        customer.CustDOB = results.getString("CustDOB");
                        customer.CustAddr = results.getString("CustAddr");
                        customer.CustContactNo = results.getString("CustContactNo");
                        customer.CustEmail = results.getString("CustEmail");
                        
                        SQLDatas.add(customer);
                    }
                    break;
                case "Employee":
                    while(results.next()) {
                        DataModel.Employee employee = new DataModel(). new Employee();
                        employee.EmpNo = results.getInt("EmpNo");
                        employee.EmpFName = results.getString("EmpFName");
                        employee.EmpLName = results.getString("EmpLName");
                        employee.EmpSex = results.getString("EmpSex").charAt(0);
                        employee.EmpDOB = results.getString("EmpDOB");
                        employee.EmpAddr = results.getString("EmpAddr");
                        employee.EmpContactNo = results.getString("EmpContactNo");
                        employee.EmpEmail = results.getString("EmpEmail");
                        employee.EmpTIN = results.getInt("EmpTIN");
                        employee.EmpHireDate = results.getString("EmpHireDate");
                        employee.EmpJobCode = results.getInt("EmpJobCode");
                        
                        SQLDatas.add(employee);
                    }
                    break;
                case "Job":
                    while(results.next()) {
                        DataModel.Job job = new DataModel(). new Job();
                        job.JobCode = results.getInt("JobCode");
                        job.JobPos = results.getString("JobPos");
                        job.JobDesc = results.getString("JobDesc");
                        job.JobSal = results.getInt("JobSal");
                        
                        SQLDatas.add(job);
                    }
                    break;
                case "Contract":
                    while(results.next()) {
                        DataModel.Contract contract = new DataModel(). new Contract();
                        contract.ContNo = results.getInt("ContNo");
                        contract.ContLockedIn = results.getBoolean("ContLockedIn");
                        contract.ContWaterDisp = results.getBoolean("ContWaterDisp");
                        contract.ContContainer = results.getBoolean("ContContainer");
                        contract.ContWatAnalysis = results.getBoolean("ContWatAnalysis");
                        contract.ContAmtPerMon = results.getInt("ContAmtPerMon");
                        contract.Duration = results.getInt("ContDuration");
                        contract.ContSDate = results.getString("ContSDate");
                        contract.ContEDate = results.getString("ContEDate");
                        contract.ContCustNo = results.getInt("ContCustNo");
                        contract.ContEmpNo = results.getInt("ContEmpNo");
                        
                        SQLDatas.add(contract);
                    }
                    break;
                case "Order_":
                    while(results.next()) {
                        DataModel.Order order = new DataModel(). new Order();
                        order.OrdNo = results.getInt("OrdNo");
                        order.OrdEmpNo = results.getInt("OrdEmpNo");
                        order.OrdDeliverDate = results.getString("OrdDeliverDate");
                        order.OrdDeliverAddr = results.getString("OrdDeliverAddr");
                        order.OrdContNo = results.getInt("OrdContNo");
                        order.OrdPayNo = results.getInt("OrdPayNo");
                        
                        SQLDatas.add(order);
                    }
                    break;
                case "Payment":
                    while(results.next()) {
                        DataModel.Payment payment = new DataModel(). new Payment();
                        payment.PayNo = results.getInt("PayNo");
                        payment.PayDate = results.getString("PayDate");
                        payment.PayAmt = results.getInt("PayAmt");
                        
                        SQLDatas.add(payment);
                    }
                    break;
                case "SupplyOrder":
                    while(results.next()) {
                        DataModel.SupplyOrder supply_order = new DataModel(). new SupplyOrder();
                        supply_order.SupplyNo = results.getInt("SupplyNo");
                        supply_order.OrdNo = results.getInt("OrdNo");
                        supply_order.SupplyOrderQty = results.getInt("SupplyOrderQty");
                        
                        SQLDatas.add(supply_order);
                    }
                    break;
                case "Supply":
                    while(results.next()) {
                        DataModel.Supply supply = new DataModel(). new Supply();
                        supply.SupplyNo = results.getInt("SupplyNo");
                        supply.SupplyName = results.getString("SupplyName");
                        supply.SupplyQOH = results.getInt("SupplyQOH");
                        supply.SupplyPrice = results.getInt("SupplyPrice");
                        
                        SQLDatas.add(supply);
                    }
                    break;
            }
            
            sql_con.close();
            
            return SQLDatas;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void UpdateItemFromTable(Object Data, String Table) {
        try
        {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            String QueryStr = "";
            
            switch(Table)
            {
                case "Customer":
                    DataModel.Customer customer = (DataModel.Customer)Data;
                    QueryStr = String.format("UPDATE Customer "
                                          + "SET CustFName = \"%s\","
                                          + "CustLName = \"%s\","
                                          + "CustSex = \"%s\","
                                          + "CustDOB = \"%s\","
                                          + "CustAddr = \"%s\","
                                          + "CustContactNo = \"%s\","
                                          + "CustEmail = \"%s\" "
                                          + "WHERE CustNo = \"%d\"",
                            customer.CustFName, customer.CustLName, customer.CustSex, customer.CustDOB, customer.CustAddr, customer.CustContactNo, customer.CustEmail, customer.CustNo);
                    break;
                case "Employee":
                    DataModel.Employee employee = (DataModel.Employee)Data;
                    QueryStr = String.format("UPDATE Employee "
                                          + "SET EmpFName = \"%s\","
                                          + "EmpLName = \"%s\","
                                          + "EmpSex = \"%s\","
                                          + "EmpDOB = \"%s\","
                                          + "EmpAddr = \"%s\","
                                          + "EmpContactNo = \"%s\","
                                          + "EmpEmail = \"%s\","
                                          + "EmpTIN = \"%d\","
                                          + "EmpHireDate = \"%s\","
                                          + "EmpJobCode = \"%d\" "
                                          + "WHERE EmpNo = \"%d\"",
                            employee.EmpFName, employee.EmpLName, employee.EmpSex, employee.EmpDOB, employee.EmpAddr, employee.EmpContactNo, employee.EmpEmail, employee.EmpTIN, employee.EmpHireDate, employee.EmpJobCode, employee.EmpNo);
                    break;
                case "Job":
                    DataModel.Job job = (DataModel.Job)Data;
                    QueryStr = String.format("UPDATE Job "
                                          + "SET JobPos = \"%s\","
                                          + "JobDesc = \"%s\","
                                          + "JobSal = \"%d\" "
                                          + "WHERE JobCode = \"%d\"",
                            job.JobPos, job.JobDesc, job.JobSal, job.JobCode);
                    break;
                case "Contract":
                    DataModel.Contract contract = (DataModel.Contract)Data;
                    if(contract.Duration != null) {
                        QueryStr = String.format("UPDATE Contract "
                                          + "SET ContLockedIn = %b,"
                                          + "ContWaterDisp = %b,"
                                          + "ContContainer = %b,"
                                          + "ContWatAnalysis = %b,"
                                          + "ContAmtPerMon = \"%d\","
                                          + "ContDuration = \"%d\","
                                          + "ContSDate = \"%s\","
                                          + "ContEDate = \"%s\","
                                          + "ContCustNo = \"%d\","
                                          + "ContEmpNo = \"%d\" "
                                          + "WHERE ContNo = \"%d\"",
                            contract.ContLockedIn, contract.ContWaterDisp, contract.ContContainer, contract.ContWatAnalysis, contract.ContAmtPerMon, contract.Duration, contract.ContSDate, contract.ContEDate, contract.ContCustNo, contract.ContEmpNo, contract.ContNo);
                    }
                    else {
                         QueryStr = String.format("UPDATE Contract "
                                          + "SET ContLockedIn = %b,"
                                          + "ContWaterDisp = %b,"
                                          + "ContContainer = %b,"
                                          + "ContWatAnalysis = %b,"
                                          + "ContAmtPerMon = \"%d\","
                                          + "ContDuration = NULL,"
                                          + "ContSDate = \"%s\","
                                          + "ContEDate = \"%s\","
                                          + "ContCustNo = \"%d\","
                                          + "ContEmpNo = \"%d\" "
                                          + "WHERE ContNo = \"%d\"",
                            contract.ContLockedIn, contract.ContWaterDisp, contract.ContContainer, contract.ContWatAnalysis, contract.ContAmtPerMon, contract.ContSDate, contract.ContEDate, contract.ContCustNo, contract.ContEmpNo, contract.ContNo);
                    }
                    break;
                case "Order_":
                    DataModel.Order order = (DataModel.Order)Data;
                    if(order.OrdPayNo != null) {
                        QueryStr = String.format("UPDATE Order_ "
                                          + "SET OrdEmpNo = \"%d\","
                                          + "OrdDeliverDate = \"%s\","
                                          + "OrdDeliverAddr = \"%s\","
                                          + "OrdContNo = \"%d\","
                                          + "OrdPayNo = \"%d\" "
                                          + "WHERE OrdNo = \"%d\"",
                            order.OrdEmpNo, order.OrdDeliverDate, order.OrdDeliverAddr, order.OrdContNo, order.OrdPayNo, order.OrdNo);
                    }
                    else {
                        QueryStr = String.format("UPDATE Order_ "
                                          + "SET OrdEmpNo = \"%d\","
                                          + "OrdDeliverDate = \"%s\","
                                          + "OrdDeliverAddr = \"%s\","
                                          + "OrdContNo = \"%d\","
                                          + "OrdPayNo = NULL "
                                          + "WHERE OrdNo = \"%d\"",
                            order.OrdEmpNo, order.OrdDeliverDate, order.OrdDeliverAddr, order.OrdContNo, order.OrdNo);
                    }
                    break;
                case "Payment":
                    DataModel.Payment payment = (DataModel.Payment)Data;
                    QueryStr = String.format("UPDATE Payment "
                                          + "SET PayDate = \"%s\","
                                          + "PayAmt = \"%d\" "
                                          + "WHERE PayNo = \"%d\"",
                            payment.PayDate, payment.PayAmt, payment.PayNo);
                    break;
                case "SupplyOrder":
                    DataModel.SupplyOrder supply_order = (DataModel.SupplyOrder)Data;
                    QueryStr = String.format("UPDATE SupplyOrder "
                                          + "SET SupplyNo = \"%d\","
                                          + "OrdNo = \"%d\","
                                          + "SupplyOrderQty = \"%d\" "
                                          + "WHERE SupplyNo = \"%d\" AND OrdNo = \"%d\"",
                            supply_order.SupplyNo, supply_order.OrdNo, supply_order.SupplyOrderQty, supply_order.OldSupplyNo, supply_order.OldOrdNo);
                    break;
                case "Supply":
                    DataModel.Supply supply = (DataModel.Supply)Data;
                    QueryStr = String.format("UPDATE Supply "
                                          + "SET SupplyName = \"%s\","
                                          + "SupplyQOH = \"%d\", "
                                          + "SupplyPrice = \"%d\" "
                                          + "WHERE SupplyNo = \"%d\"",
                            supply.SupplyName, supply.SupplyQOH, supply.SupplyPrice, supply.SupplyNo);
                    break;
            }
            
            Statement SetDB = sql_con.createStatement();
            SetDB.executeUpdate("USE crud_app;");
            
            Statement Query = sql_con.createStatement();
            Query.executeUpdate(QueryStr);
            
            sql_con.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void DeleteItemFromTable(Object Data, String Table) {
        try
        {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            String QueryStr = "";
            
            switch(Table)
            {
                case "Customer":
                    DataModel.Customer customer = (DataModel.Customer)Data;
                    QueryStr = String.format("DELETE FROM Customer "
                                            + "WHERE CustNo=\"%d\"", customer.CustNo);
                    break;
                case "Employee":
                    DataModel.Employee employee = (DataModel.Employee)Data;
                    QueryStr = String.format("DELETE FROM Employee "
                                            + "WHERE EmpNo=\"%d\"", employee.EmpNo);
                    break;
                case "Job":
                    DataModel.Job job = (DataModel.Job)Data;
                    QueryStr = String.format("DELETE FROM Job "
                                            + "WHERE JobCode=\"%d\"", job.JobCode);
                    break;
                case "Contract":
                    DataModel.Contract contract = (DataModel.Contract)Data;
                    QueryStr = String.format("DELETE FROM Contract "
                                            + "WHERE ContNo=\"%d\"", contract.ContNo);
                    break;
                case "Order_":
                    DataModel.Order order = (DataModel.Order)Data;
                    QueryStr = String.format("DELETE FROM Order_ "
                                            + "WHERE OrdNo=\"%d\"", order.OrdNo);
                    break;
                case "Payment":
                    DataModel.Payment payment = (DataModel.Payment)Data;
                    QueryStr = String.format("DELETE FROM Payment "
                                            + "WHERE PayNo=\"%d\"", payment.PayNo);
                    break;
                case "SupplyOrder":
                    DataModel.SupplyOrder supply_order = (DataModel.SupplyOrder)Data;
                    QueryStr = String.format("DELETE FROM SupplyOrder "
                                            + "WHERE SupplyNo=\"%d\" AND OrdNo=\"%d\"", supply_order.SupplyNo, supply_order.OrdNo);
                    break;
                case "Supply":
                    DataModel.Supply supply = (DataModel.Supply)Data;
                    QueryStr = String.format("DELETE FROM Supply "
                                            + "WHERE SupplyNo=\"%d\"", supply.SupplyNo);
                    break;
            }
            
            Statement SetDB = sql_con.createStatement();
            SetDB.executeUpdate("USE crud_app;");
            
            Statement Query = sql_con.createStatement();
            Query.executeUpdate(QueryStr);
            
            sql_con.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Remove(String Type, String Name) {
        try
        {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            String QueryStr = "";
            
            switch(Type) {
                case "Table Contents":
                    QueryStr = String.format("TRUNCATE TABLE %s", Name);
                    break;
                case "Database":
                    QueryStr = String.format("DROP DATABASE %s", Name);
                    break;
            }
            
            Statement SetDB = sql_con.createStatement();
            SetDB.executeUpdate("USE crud_app;");
            
            Statement Query = sql_con.createStatement();
            Query.executeUpdate(QueryStr);
            
            sql_con.close();
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    //// PRIVATE FUNCTIONS ////
    
    // Source: https://stackoverflow.com/questions/838978/how-to-check-if-mysql-database-exists
    private boolean isDBExists(String DatabaseName) {
        try {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
        
            try (ResultSet resultSet = sql_con.getMetaData().getCatalogs()) {
                while (resultSet.next()) {
                    String databaseName = resultSet.getString(1);
                    if(databaseName.equals(DatabaseName)){
                        return true;
                    }
                }
            }
        }
        catch(SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    // Source: https://stackoverflow.com/questions/838978/how-to-check-if-mysql-database-exists
    private boolean isTableExists(String TableName) {
        try {
            Connection sql_con = DriverManager.getConnection(LINK, USERNAME, PASSWORD);
            
            Statement Query = sql_con.createStatement();
            Query.executeUpdate(
                    "USE crud_app;"
            );
            
            DatabaseMetaData meta = sql_con.getMetaData();
            ResultSet resultSet = meta.getTables("crud_app", null, TableName, new String[] {"TABLE"});
            return resultSet.next();
        
        }
        catch(SQLException ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
}
