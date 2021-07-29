package model;

public class DocRejection {
    int doc_id;
    String failed_explanation;
    public int getDoc_id() {
        return doc_id;
    }
    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }
    public String getFailed_explanation() {
        return failed_explanation;
    }
    public void setFailed_explanation(String failed_explanation) {
        this.failed_explanation = failed_explanation;
    }
}
