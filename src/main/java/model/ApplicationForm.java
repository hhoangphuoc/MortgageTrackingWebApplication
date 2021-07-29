package model;

import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

import util.ObjectUtil;

@XmlRootElement
public class ApplicationForm {
	public int size(){
        return 28;
    }
    public int insertSize(){
        return 28;
    }
    
    private int c_id;
    private int m_id;
    private float grossIncome;
    private boolean willRetired;
    private boolean havePartner;
    private float partnerGrossIncome;
    private boolean willPartnerRetire;
    private boolean haveStudentLoan;
    private float studentDebt;
    private boolean haveSpousalMaintenance;
    private float monthlyAlimony;
    private boolean haveOtherDebt;
    private String otherDebtInfo;
    private float otherDebtAmount;
    private boolean haveCredits;
    private String creditInfo;
    private float creditAmount;
    private float bankAmount;
    private String collateral1;
    private float price1;
    private String collateral2;
    private float price2;
    private String collateral3;
    private float price3;
    private float requestedMortgageAmount;
    private String date;
    private String duration;
    private String reason;
    
    public int getC_id(){
        return this.c_id;
    }
    public void setC_id(int c_id){
        this.c_id = c_id;
    }
    
    public int getM_id(){
        return this.m_id;
    }
    public void setM_id(int m_id){
        this.m_id = m_id;
    }
    
    public float getGrossIncome(){
        return this.grossIncome;
    }
    public void setGrossIncome(float grossIncome){
        this.grossIncome = grossIncome;
    }
    
    public boolean getWillRetired(){
        return this.willRetired;
    }
    public void setWillRetired(boolean willRetired){
        this.willRetired = willRetired;
    }
    
    public boolean getHavePartner(){
        return this.havePartner;
    }
    public void setHavePartner(boolean havePartner){
        this.havePartner = havePartner;
    }
    
    public float getPartnerGrossIncome(){
        return this.partnerGrossIncome;
    }
    public void setPartnerGrossIncome(float partnerGrossIncome){
        this.partnerGrossIncome = partnerGrossIncome;
    }
    
    public boolean getWillPartnerRetire(){
        return this.willPartnerRetire;
    }
    public void setWillPartnerRetire(boolean willPartnerRetire){
        this.willPartnerRetire = willPartnerRetire;
    }
    
    public boolean getHaveStudentLoan(){
        return this.haveStudentLoan;
    }
    public void setHaveStudentLoan(boolean haveStudentLoan){
        this.haveStudentLoan = haveStudentLoan;
    }
    
    public float getStudentDebt(){
        return this.studentDebt;
    }
    public void setStudentDebt(float studentDebt){
        this.studentDebt = studentDebt;
    }
    
    public boolean getHaveSpousalMaintenance(){
        return this.haveSpousalMaintenance;
    }
    public void setHaveSpousalMaintenance(boolean haveSpousalMaintenance){
        this.haveSpousalMaintenance = haveSpousalMaintenance;
    }
    
    public float getMonthlyAlimony(){
        return this.monthlyAlimony;
    }
    public void setMonthlyAlimony(float monthlyAlimony){
        this.monthlyAlimony = monthlyAlimony;
    }
    
    public boolean getHaveOtherDebt(){
        return this.haveOtherDebt;
    }
    public void setHaveOtherDebt(boolean haveOtherDebt){
        this.haveOtherDebt = haveOtherDebt;
    }
    
    public String getOtherDebtInfo(){
        return this.otherDebtInfo;
    }
    public void setOtherDebtInfo(String otherDebtInfo){
        this.otherDebtInfo = otherDebtInfo;
    }
    
    public float getOtherDebtAmount(){
        return this.otherDebtAmount;
    }
    public void setOtherDebtAmount(float otherDebtAmount){
        this.otherDebtAmount = otherDebtAmount;
    }
    
    public boolean getHaveCredits(){
        return this.haveCredits;
    }
    public void setHaveCredits(boolean haveCredits){
        this.haveCredits = haveCredits;
    }
    
    public String getCreditInfo(){
        return this.creditInfo;
    }
    public void setCreditInfo(String creditInfo){
        this.creditInfo = creditInfo;
    }
    
    public float getCreditAmount(){
        return this.creditAmount;
    }
    public void setCreditAmount(float creditAmount){
        this.creditAmount = creditAmount;
    }
    
    public float getBankAmount(){
        return this.bankAmount;
    }
    public void setBankAmount(float bankAmount){
        this.bankAmount = bankAmount;
    }
    
    public String getCollateral1(){
        return this.collateral1;
    }
    public void setCollateral1(String collateral1){
        this.collateral1 = collateral1;
    }
    
    public float getPrice1(){
        return this.price1;
    }
    public void setPrice1(float price1){
        this.price1 = price1;
    }
    
    public String getCollateral2(){
        return this.collateral2;
    }
    public void setCollateral2(String collateral2){
        this.collateral2 = collateral2;
    }
    
    public float getPrice2(){
        return this.price2;
    }
    public void setPrice2(float price2){
        this.price2 = price2;
    }
    
    public String getCollateral3(){
        return this.collateral3;
    }
    public void setCollateral3(String collateral3){
        this.collateral3 = collateral3;
    }
    
    public float getPrice3(){
        return this.price3;
    }
    public void setPrice3(float price3){
        this.price3 = price3;
    }
    
    public float getRequestedMortgageAmount(){
        return this.requestedMortgageAmount;
    }
    public void setRequestedMortgageAmount(float requestedMortgageAmount){
        this.requestedMortgageAmount = requestedMortgageAmount;
    }
    
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    
    public String getDuration(){
        return this.duration;
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    
    public String getReason(){
        return this.reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }
    
}

