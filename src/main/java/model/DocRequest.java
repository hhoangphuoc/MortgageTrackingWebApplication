package model;

public class DocRequest extends BaseModel {
    int doc_id;
    int mortgage_id;
    String document_type;
    String document_status;
    String failed_explanation;

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public int getMortgage_id() {
        return mortgage_id;
    }

    public void setMortgage_id(int mortgage_id) {
        this.mortgage_id = mortgage_id;
    }

    public String getDocument_type() {
        return document_type;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public String getDocument_status() {
        return document_status;
    }

    public void setDocument_status(String document_status) {
        this.document_status = document_status;
    }

    public String getFailed_explanation() {
        return failed_explanation;
    }

    public void setFailed_explanation(String failed_explanation) {
        this.failed_explanation = failed_explanation;
    }
}
