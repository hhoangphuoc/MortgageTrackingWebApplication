package dao;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.contracts.IMortgageCheckDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.MortgageCheck;

public class MortgageCheckDao extends BaseDao implements IMortgageCheckDao {

    public MortgageCheckDao(Transaction transaction) {
        super(transaction);
    }
    
    static MortgageCheck resultToModel(ResultSet resultSet)
        throws SQLException
    {
        int i = 1;
        var mortgageCheck = new MortgageCheck();
        mortgageCheck.setM_id(resultSet.getInt(i++));
        mortgageCheck.setFailedExplannation(resultSet.getString(i++));
        mortgageCheck.setIsIncomeAccepted(resultSet.getBoolean(i++));
        mortgageCheck.setIsCollateralAccepted(resultSet.getBoolean(i++));
        mortgageCheck.setIsFiniancialObligationsAccepted(resultSet.getBoolean(i++));
        
        return mortgageCheck;
    }

    private final String GET_BY_M_ID_QUERY
            = "SELECT m_id, failedExplannation, isIncomeAccepted, isCollateralAccepted, isFiniancialObligationsAccepted\n" +
            "FROM mortgage_check\n" +
            "WHERE m_id = ?\n";

    public MortgageCheck getByMId
        (int m_id)
        throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(GET_BY_M_ID_QUERY);
            statement.setInt(1, m_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultToModel(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    private final String UPDATE_QUERY
            = "UPDATE mortgage_check \n" +
            "SET failedExplannation = ?, isIncomeAccepted = ?, isCollateralAccepted = ?, isFiniancialObligationsAccepted= ?\n" +
            "WHERE m_id = ?";
    
    public int update(MortgageCheck mortgageCheck)
        throws DaoInternalServerException
	{
	    try {
            var statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, mortgageCheck.getFailedExplannation());
            statement.setBoolean(2, mortgageCheck.getIsIncomeAccepted());
            statement.setBoolean(3, mortgageCheck.getIsCollateralAccepted());
            statement.setBoolean(4, mortgageCheck.getIsFiniancialObligationsAccepted());
            statement.setInt(5, mortgageCheck.getM_id());
            statement.executeUpdate();
            return statement.getUpdateCount();
        } catch (SQLException e){
	        throw new DaoInternalServerException(e);
        }
	}
}
