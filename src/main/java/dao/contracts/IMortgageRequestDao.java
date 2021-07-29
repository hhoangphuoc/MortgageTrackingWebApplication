package dao.contracts;

import dao.exceptions.DaoInternalServerException;
import model.MortgageRequest;
import model.MortgageStatus;
import org.json.JSONArray;

import java.util.List;

public interface IMortgageRequestDao {
    MortgageStatus getByMIdWithStatus(int param) throws DaoInternalServerException;
    List<MortgageRequest> getAllMortgageRequestsbyCustomer(int customerID) throws DaoInternalServerException;
    List<MortgageStatus> getAllByCustomerWithStatus(int customerId) throws DaoInternalServerException;
    List<MortgageRequest> getAllMortgageRequestsbySP(int spID) throws DaoInternalServerException;
    JSONArray getMortgageData(int spId) throws DaoInternalServerException;
    void deleteMortgageRequest(int mortgageID) throws DaoInternalServerException;
}
