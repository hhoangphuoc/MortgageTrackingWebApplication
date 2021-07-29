package model;

import java.util.ArrayList;
import java.util.List;

public class DocRequestWithFiles extends DocRequest {
    public DocRequestWithFiles(DocRequest newDoc){
        this.doc_id = newDoc.doc_id;
        this.mortgage_id = newDoc.mortgage_id;
        this.document_type = newDoc.document_type;
        this.document_status = newDoc.document_status;
        this.failed_explanation = newDoc.failed_explanation;
    }


    private List<FileMeta> files = new ArrayList<>();

    public List<FileMeta> getFiles(){
        return files;
    }
}
