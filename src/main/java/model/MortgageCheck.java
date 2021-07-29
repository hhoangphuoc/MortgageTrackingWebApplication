package model;

public class MortgageCheck {
    
    public int size(){
        return 5;
    }
    public int insertSize(){
        return 5;
    }
    
    private int m_id;
    private String failedExplannation;
    private boolean isIncomeAccepted;
    private boolean isCollateralAccepted;
    private boolean isFiniancialObligationsAccepted;
    
    public MortgageCheck() {}
    
//    public MortgageCheck(int m_id, String failedExplannation, boolean isIncomeAccepted, 
//    		boolean isCollateralAccepted, boolean isFiniancialObligationsAccepted) {
//    	this.m_id = m_id;
//    	this.failedExplannation = failedExplannation;
//    	this.isIncomeAccepted = isIncomeAccepted;
//    	this.isCollateralAccepted = isCollateralAccepted;
//    	this.isFiniancialObligationsAccepted = isFiniancialObligationsAccepted;
//    }
    
    public int getM_id(){
        return this.m_id;
    }
    public void setM_id(int m_id){
        this.m_id = m_id;
    }
    
    public String getFailedExplannation(){
        return this.failedExplannation;
    }
    public void setFailedExplannation(String failedExplannation){
        this.failedExplannation = failedExplannation;
    }
    
    public boolean getIsIncomeAccepted(){
        return this.isIncomeAccepted;
    }
    public void setIsIncomeAccepted(boolean isIncomeAccepted){
        this.isIncomeAccepted = isIncomeAccepted;
    }
    
    public boolean getIsCollateralAccepted(){
        return this.isCollateralAccepted;
    }
    public void setIsCollateralAccepted(boolean isCollateralAccepted){
        this.isCollateralAccepted = isCollateralAccepted;
    }
    
    public boolean getIsFiniancialObligationsAccepted(){
        return this.isFiniancialObligationsAccepted;
    }
    public void setIsFiniancialObligationsAccepted(boolean isFiniancialObligationsAccepted){
        this.isFiniancialObligationsAccepted = isFiniancialObligationsAccepted;
    }
    
}
