package model;

public class FileData extends FileMeta {
    private byte[] file_data;

    
    public byte[] getFile_data(){
        return this.file_data;
    }
    public void setFile_data(byte[] file_data){
        this.file_data = file_data;
    }
    
}
