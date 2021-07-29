package dao.contracts;

import dao.exceptions.DaoInternalServerException;
import model.InterestOffer;

public interface IInterestOfferDao {
    InterestOffer getByMId (int m_id) throws DaoInternalServerException;
    void insert(InterestOffer model) throws DaoInternalServerException;
    int update(InterestOffer model) throws DaoInternalServerException;
    void createInterestOffer(int mortgageID, String duration) throws DaoInternalServerException;
}
