/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.it131_group6.crud_app;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hp
 */

// Make Singleton
public class ApplicationHelper {
    
    private DatabaseService db_service;
    private static ApplicationHelper s_instance;
    private ApplicationHelper() {
        db_service = new DatabaseService();
    }
    
    public void InitEnv() {
        db_service.InitializeEnv();
    }
    
    public static ApplicationHelper getHelper() {
        if(s_instance == null)
            s_instance = new ApplicationHelper();
        return s_instance;
    }
    
    public boolean AddNewItem(Object Data, String Table) {
        try {
            // Pass Inputs to Database
            db_service.AddItemToTable(Data, Table);
            
            return true;
        }
        catch(Exception ex) {
            Logger.getLogger(DatabaseService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public void ReadExistingItems(JTable SQLTable, int CurrentTabIndex) {
        String CurrentTab = "";
        DefaultTableModel DTM = new DefaultTableModel();
        ArrayList<Object> arrayList;
        
        // Table Model Modification
        switch(CurrentTabIndex) {
            case 0:
                CurrentTab = "Customer";
                DTM.setColumnIdentifiers(new Object[] {"CustNo", "CustFName", "CustLName", "CustSex", "CustDOB", "CustAddr", "CustContactNo", "CustEmail"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(customer -> (DataModel.Customer)customer).forEachOrdered(customerHolder -> {
                    DTM.addRow(new Object[] {customerHolder.CustNo, customerHolder.CustFName, customerHolder.CustLName, customerHolder.CustSex, customerHolder.CustDOB, customerHolder.CustAddr, customerHolder.CustContactNo, customerHolder.CustEmail});
                });
                break;
            case 1:
                CurrentTab = "Employee";
                DTM.setColumnIdentifiers(new Object[] {"EmpNo", "EmpFName", "EmpLName", "EmpSex", "EmpDOB", "EmpAddr", "EmpContactNo", "EmpEmail", "EmpTIN", "EmpHireDate", "EmpJobCode"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(employee -> (DataModel.Employee)employee).forEachOrdered(employeeHolder -> {
                    DTM.addRow(new Object[] {employeeHolder.EmpNo, employeeHolder.EmpFName, employeeHolder.EmpLName, employeeHolder.EmpSex, employeeHolder.EmpDOB, employeeHolder.EmpAddr, employeeHolder.EmpContactNo, employeeHolder.EmpEmail, employeeHolder.EmpTIN, employeeHolder.EmpHireDate, employeeHolder.EmpJobCode});
                });
                break;
            case 2:
                CurrentTab = "Job";
                DTM.setColumnIdentifiers(new Object[] {"JobCode", "JobPos", "JobDesc", "JobSal"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(job -> (DataModel.Job)job).forEachOrdered(jobHolder -> {
                    DTM.addRow(new Object[] {jobHolder.JobCode, jobHolder.JobPos, jobHolder.JobDesc, jobHolder.JobSal});
                });
                break;
            case 3:
                CurrentTab = "Contract";
                DTM.setColumnIdentifiers(new Object[] {"ContNo", "ContLockedIn", "ContWaterDisp", "ContContainer", "ContWatAnalysis", "ContAmtPerMon", "ContDuration", "ContSDate", "ContEDate", "ContCustNo", "ContEmpNo" });
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(contract -> (DataModel.Contract)contract).forEachOrdered(contractHolder -> {
                    DTM.addRow(new Object[] {contractHolder.ContNo, contractHolder.ContLockedIn, contractHolder.ContWaterDisp, contractHolder.ContContainer, contractHolder.ContWatAnalysis, contractHolder.ContAmtPerMon, contractHolder.Duration, contractHolder.ContSDate, contractHolder.ContEDate, contractHolder.ContCustNo, contractHolder.ContEmpNo});
                });
                break;
            case 4:
                CurrentTab = "Order_";
                DTM.setColumnIdentifiers(new Object[] {"OrdNo", "OrdEmpNo", "OrdDeliverDate", "OrdDeliverAddr", "OrdContNo", "OrdPayNo"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(order -> (DataModel.Order)order).forEachOrdered(orderHolder -> {
                    DTM.addRow(new Object[] {orderHolder.OrdNo, orderHolder.OrdEmpNo, orderHolder.OrdDeliverDate, orderHolder.OrdDeliverAddr, orderHolder.OrdContNo, orderHolder.OrdPayNo});
                });
                break;
            case 5:
                CurrentTab = "Payment";
                DTM.setColumnIdentifiers(new Object[] {"PayNo", "PayDate", "PayAmt"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(payment -> (DataModel.Payment)payment).forEachOrdered(paymentHolder -> {
                    DTM.addRow(new Object[] {paymentHolder.PayNo, paymentHolder.PayDate, paymentHolder.PayAmt});
                });
                break;
            case 6:
                CurrentTab = "SupplyOrder";
                DTM.setColumnIdentifiers(new Object[] {"SupplyNo", "OrdNo", "SupplyOrderQuantity"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(supply_order -> (DataModel.SupplyOrder)supply_order).forEachOrdered(supplyOrderHolder -> {
                    DTM.addRow(new Object[] {supplyOrderHolder.SupplyNo, supplyOrderHolder.OrdNo, supplyOrderHolder.SupplyOrderQty});
                });
                break;
            case 7:
                CurrentTab = "Supply";
                DTM.setColumnIdentifiers(new Object[] {"SupplyNo", "SupplyName", "SupplyQOH", "SupplyPrice"});
                
                arrayList = db_service.ReadItemsFromTable(CurrentTab);
                arrayList.stream().map(supply -> (DataModel.Supply)supply).forEachOrdered(supplyHolder -> {
                    DTM.addRow(new Object[] {supplyHolder.SupplyNo, supplyHolder.SupplyName, supplyHolder.SupplyQOH, supplyHolder.SupplyPrice});
                });
                break;
        }
        
        SQLTable.setModel(DTM);
    }
    
    public boolean UpdateExistingItem(Object NewData, String Table) {
        // Check Inputs
        // Pass Inputs to Database
        db_service.UpdateItemFromTable(NewData, Table);
        
        return true;
    }
    
    public boolean DeleteExistingItem(Object Data, String Table) {
        // Check Inputs
        // Pass Inputs to Database
        db_service.DeleteItemFromTable(Data, Table);
        
        return true;
    }
}
