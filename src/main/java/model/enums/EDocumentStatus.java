package model.enums;

public enum EDocumentStatus {
    PENDING("Pending"), SUBMITTED("Submitted"), ACCEPTED("Accepted"), REJECTED("Rejected");
    String value;
    EDocumentStatus(String value){
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
