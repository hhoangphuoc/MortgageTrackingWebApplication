package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.contracts.IInterestOfferDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.InterestOffer;

public class InterestOfferDao extends BaseDao implements IInterestOfferDao {


	public InterestOfferDao(Transaction transaction) {
		super(transaction);
	}
    
    public void modelToStatement
        (PreparedStatement statement, InterestOffer interestOffer)
        throws SQLException
    {
        int i = 1;
        statement.setInt(i++, interestOffer.getM_id());
        statement.setString(i++, interestOffer.getDuration());
        statement.setDouble(i++, interestOffer.getInterest_rate());
    }
    
    public static InterestOffer resultToModel
        (ResultSet resultSet)
        throws SQLException
    {
        return resultToModel(resultSet, 1);
    }
    
    public static InterestOffer resultToModel
        (ResultSet resultSet, int i)
        throws SQLException
    {
        var interestOffer = new InterestOffer();
        interestOffer.setM_id(resultSet.getInt(i++));
        interestOffer.setDuration(resultSet.getString(i++));
        interestOffer.setInterest_rate(resultSet.getDouble(i++));
        
        return interestOffer;
    }

    private final String GET_BY_M_ID_QUERY
        = "SELECT m_id, duration, interest_rate " +
        "FROM interest_offer " +
        "WHERE m_id = ?";

    public InterestOffer getByMId (int m_id) throws DaoInternalServerException
    {
        try {
			var statement = connection.prepareStatement(GET_BY_M_ID_QUERY);
			statement.setInt(1, m_id);
        	ResultSet resultSet = statement.executeQuery();
        	if (resultSet.next()){
            	return resultToModel(resultSet);
        	}
			return null;
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    private final String INSERT_QUERY
        = "INSERT INTO interest_offer \n" +
        "(m_id, duration, interest_rate)\n" +
        "VALUES (?, ?, ?)\n";

    public void insert(InterestOffer model) throws DaoInternalServerException {
        try {
			var statement = connection.prepareStatement(INSERT_QUERY);
	        this.modelToStatement(statement, model);
	        statement.execute();
		} catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }    

    private final String UPDATE_QUERY
        = "UPDATE interest_offer \n" +
        "SET duration = ?, interest_rate = ?\n" +
        "WHERE m_id = ?";

    public int update(InterestOffer model) throws DaoInternalServerException {
        try {
			PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
	        statement.setString(1, model.getDuration());
	        statement.setDouble(2, model.getInterest_rate());
	    	statement.setInt(3, model.getM_id());
	        return statement.executeUpdate();
		} catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }
	public void createInterestOffer(int mortgageID, String duration) throws DaoInternalServerException {
		InterestOffer newInterestOffer = new InterestOffer(mortgageID, duration, getInterestRateByDuration(duration));
		insert(newInterestOffer);
	}
    
    private double getInterestRateByDuration(String duration) {
        if (duration == null){duration="";}
    	switch (duration) {
    		case "1 months":
    			return 0.4;
    		case "6 months":
    			return 0.6;
    		case "12 months":
    			return 0.8;
    		case "2 years":
    			return 1;
    		case "5 years":
    			return 1.2;
    		case "10 years":
    			return 1.4;
    		default: 
    			return 0;
    	}
    }
}
