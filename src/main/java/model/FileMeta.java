package model;

public class FileMeta extends BaseModel {
    private int file_id;
    private int doc_id;
    private String file_name;
    private String file_type;
    
    public int getFile_id(){
        return this.file_id;
    }
    public void setFile_id(int file_id){
        this.file_id = file_id;
    }
    
    public int getDoc_id(){
        return this.doc_id;
    }
    public void setDoc_id(int doc_id){
        this.doc_id = doc_id;
    }
    
    public String getFile_name(){
        return this.file_name;
    }
    public void setFile_name(String file_name){
        this.file_name = file_name;
    }
    
    public String getFile_type(){
        return this.file_type;
    }
    public void setFile_type(String file_type){
        this.file_type = file_type;
    }
}
