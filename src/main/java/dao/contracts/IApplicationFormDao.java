package dao.contracts;

import dao.exceptions.DaoInternalServerException;
import model.ApplicationForm;

import java.util.List;

public interface IApplicationFormDao {

    int createApplication(ApplicationForm applicationForm) throws DaoInternalServerException;
    ApplicationForm getApplicationFormForMortgage(int mid) throws DaoInternalServerException;
    List<ApplicationForm> getAllApplicationForms() throws DaoInternalServerException;
    void createStateTimestamp(int mortgageID) throws DaoInternalServerException;
    void createMortgageCheck(int mortgageID) throws DaoInternalServerException;
    int createMortgage(int customerID) throws DaoInternalServerException;
}
