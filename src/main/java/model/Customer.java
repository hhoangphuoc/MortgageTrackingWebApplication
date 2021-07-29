package model;
public class Customer {
    
    public int size(){
        return 4;
    }
    public int insertSize(){
        return 4;
    }
    
    private int c_id;
    private double requested_amount;
    private double aprx_gross_income;
    private String bank_account;
    
    public int getC_id(){
        return this.c_id;
    }
    public void setC_id(int c_id){
        this.c_id = c_id;
    }
    
    public double getRequested_amount(){
        return this.requested_amount;
    }
    public void setRequested_amount(double requested_amount){
        this.requested_amount = requested_amount;
    }
    
    public double getAprx_gross_income(){
        return this.aprx_gross_income;
    }
    public void setAprx_gross_income(double aprx_gross_income){
        this.aprx_gross_income = aprx_gross_income;
    }
    
    public String getBank_account(){
        return this.bank_account;
    }
    public void setBank_account(String bank_account){
        this.bank_account = bank_account;
    }
    
}
