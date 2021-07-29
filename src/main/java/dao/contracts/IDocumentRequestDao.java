package dao.contracts;

import java.util.List;
import java.util.Optional;

import dao.exceptions.DaoInternalServerException;
import model.DocRequestCreate;

import model.DocRequest;

public interface IDocumentRequestDao {
    int insert(DocRequestCreate newDoc) throws DaoInternalServerException;
    Optional<DocRequest> get(int doc_id) throws DaoInternalServerException;
//    Optional<DocRequest> getWithFileMeta(int doc_id) throws DaoInternalServerException;
    List<DocRequest> getAllByM_id(int m_id) throws DaoInternalServerException;
    void submit(int doc_id) throws DaoInternalServerException;
    void accept(int doc_id) throws DaoInternalServerException;
    void reject(int doc_id, String failed_explanation) throws DaoInternalServerException;
}
