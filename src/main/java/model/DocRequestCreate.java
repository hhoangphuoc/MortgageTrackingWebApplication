package model;

import javax.validation.constraints.NotNull;

public class DocRequestCreate extends BaseModel {
    int mortgage_id;
    @NotNull
    String document_type;
    public DocRequestCreate(){
        super();
    }

    public DocRequestCreate(int mortgage_id, String document_type){
        super();
        this.mortgage_id = mortgage_id;
        this.document_type = document_type;
    }

    public int getMortgage_id() {
        return mortgage_id;
    }
    public void setMortgage_id(int mortgage_id) {
        this.mortgage_id = mortgage_id;
    }
    public @NotNull String getDocument_type() {
        return document_type;
    }
    public void setDocument_type(@NotNull String document_type) {
        this.document_type = document_type;
    }
}
