package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.contracts.IDocumentRequestDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.DocRequest;
import model.DocRequestCreate;
import model.enums.EDocumentStatus;

public class DocumentRequestDao extends BaseDao implements IDocumentRequestDao {

    public DocumentRequestDao(Transaction transaction) {
        super(transaction);
    }

    private static final String createQuery = "INSERT INTO document_request\n"
        + "(mortgage_id, document_type, document_status)\n"
        + "VALUES (?, ?, ?) RETURNING doc_id";
    @Override
    public int insert(DocRequestCreate newDoc) throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(createQuery);
            int i = 1;
            statement.setInt(i++, newDoc.getMortgage_id());
            statement.setString(i++, newDoc.getDocument_type());
            statement.setString(i++, EDocumentStatus.PENDING.getValue());
            var result =  statement.executeQuery();
            if (!result.next()){
                throw new DaoInternalServerException("Unable to get doc_id of inserted document request");
            } else {
                return result.getInt(1);
            }
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }
    private static final String getQuery = "SELECT doc_id, mortgage_id, document_type, document_status, failed_explanation\n"
            + "FROM document_request\n";

    private List<DocRequest> get(String where, int doc_id) throws DaoInternalServerException {
        try {
            var statement = connection.prepareStatement(getQuery + where + " ORDER BY doc_id");
            statement.setInt(1, doc_id);
            var result = statement.executeQuery();
            List<DocRequest> docList = new ArrayList<>();
            while (result.next()) {
                var newDoc = new DocRequest();
                int i = 1;
                newDoc.setDoc_id(result.getInt(i++));
                newDoc.setMortgage_id(result.getInt(i++));
                newDoc.setDocument_type(result.getString(i++));
                newDoc.setDocument_status(result.getString(i++));
                newDoc.setFailed_explanation(result.getString(i++));
                docList.add(newDoc);
            }
            return docList;
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }

    @Override
    public Optional<DocRequest> get(int doc_id) throws DaoInternalServerException {
        var list = get("WHERE doc_id = ?", doc_id);
        if (list.size() < 1) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }
    
    @Override
    public List<DocRequest> getAllByM_id(int m_id) throws DaoInternalServerException {
        return get("WHERE mortgage_id = ?", m_id);
    }
    static final String setStatusQuery = "UPDATE document_request\n" +
            "SET document_status = ?\n" +
            "WHERE doc_id = ?";
    void setStatus(int doc_id, EDocumentStatus status) throws DaoInternalServerException {
        try {
            var statement = connection.prepareStatement(setStatusQuery);
            int i = 1;
            statement.setString(i++, status.getValue());
            statement.setInt(i++, doc_id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }

    @Override
    public void submit(int doc_id) throws DaoInternalServerException {
        setStatus(doc_id, EDocumentStatus.SUBMITTED);
    }
    @Override
    public void accept(int doc_id) throws DaoInternalServerException {

        setStatus(doc_id, EDocumentStatus.ACCEPTED);
    }

    static final String rejectQuery = "UPDATE document_request\n" +
            "SET document_status = ?, failed_explanation = ?\n" +
            "WHERE doc_id = ?";
    @Override
    public void reject(int doc_id, String failed_explanation) throws DaoInternalServerException {
        try {
            var statement = connection.prepareStatement(rejectQuery);
            int i = 1;
            statement.setString(i++, EDocumentStatus.REJECTED.getValue());
            statement.setString(i++, failed_explanation);
            statement.setInt(i++, doc_id);
            statement.executeUpdate();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }


}
