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
public class DataModel {
    public class Customer {
        public int CustNo;
        public String CustFName;
        public String CustLName;
        public char CustSex;
        public String CustDOB;
        public String CustAddr;
        public int CustContactNo;
        public String CustEmail;
    }
    
    public class Employee {
        public int EmpNo;
        public String EmpFName;
        public String EmpLName;
        public char EmpSex;
        public String EmpDOB;
        public String EmpAddr;
        public String EmpContactNo;
        public String EmpEmail;
        public int EmpTIN;
        public String EmpHireDate;
        public int EmpJobCode;
    }
    
    public class Contract {
        public int ContNo;
        public boolean ContLockedIn;
        public boolean ContWaterDisp;
        public boolean ContContainer;
        public boolean ContWatAnalysis;
        public int ContAmtPerMon;
        public int Duration;
        public String ContSDate;
        public String ContEDate;
        public int ContCustNo;
        public int ContEmpNo;
    }
    
    public class Order {
        public int OrdNo;
        public int OrdEmpNo;
        public String OrdDeliverDate;
        public String OrdDeliverAddr;
        public int OrdContNo;
        public int OrdPayNo;
    }
    
    public class Payment {
        public int PayNo;
        public String PayDate;
        public int PayAmt;
    }
    
    public class Job {
        public int JobCode;
        public String JobPos;
        public String JobDesc;
        public String JobSal;
    }
    
    public class SupplyOrder {
        public int SupplyNo;
        public int OrdNo;
        public int SupplyOrderQty;
    }
    
    public class Supply {
        public int SupplyNo;
        public String SupplyName;
        public int SupplyQOH;
        public int SupplyPrice;
    }
}
