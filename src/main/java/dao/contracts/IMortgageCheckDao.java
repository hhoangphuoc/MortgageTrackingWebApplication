package dao.contracts;

import dao.exceptions.DaoInternalServerException;
import model.MortgageCheck;

public interface IMortgageCheckDao {
    MortgageCheck getByMId(int m_id) throws DaoInternalServerException;
    int update(MortgageCheck mortgageCheck) throws DaoInternalServerException;
}
