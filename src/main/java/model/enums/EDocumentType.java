package model.enums;

public enum EDocumentType {
    GROSS_INCOME("Gross Income"),
    FINANCIAL_OBLIGATIONS("Financial Obligations"),
    COLLATERAL("Collateral");
    String value;
    EDocumentType(String value){
        this.value = value;
    }
    public String getValue(){
        return this.value;
    }
    @Override
    public String toString() {
        return this.value;
    }
}
