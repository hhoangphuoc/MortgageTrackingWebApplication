package dao;

import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.ApplicationForm;
import java.util.ArrayList;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.contracts.IApplicationFormDao;

public class ApplicationFormDao extends BaseDao implements IApplicationFormDao {


    public ApplicationFormDao(Transaction transaction) {
        super(transaction);
    }
        
    void modelToStatement(PreparedStatement statement, ApplicationForm application) throws SQLException
    {
        int i = 1;
        statement.setInt(i++, application.getC_id());
        statement.setInt(i++, application.getM_id());
        statement.setFloat(i++, application.getGrossIncome());
        statement.setBoolean(i++, application.getWillRetired());
        statement.setBoolean(i++, application.getHavePartner());
        statement.setFloat(i++, application.getPartnerGrossIncome());
        statement.setBoolean(i++, application.getWillPartnerRetire());
        statement.setBoolean(i++, application.getHaveStudentLoan());
        statement.setFloat(i++, application.getStudentDebt());
        statement.setBoolean(i++, application.getHaveSpousalMaintenance());
        statement.setFloat(i++, application.getMonthlyAlimony());
        statement.setBoolean(i++, application.getHaveOtherDebt());
        statement.setString(i++, application.getOtherDebtInfo());
        statement.setFloat(i++, application.getOtherDebtAmount());
        statement.setBoolean(i++, application.getHaveCredits());
        statement.setString(i++, application.getCreditInfo());
        statement.setFloat(i++, application.getCreditAmount());
        statement.setFloat(i++, application.getBankAmount());
        statement.setString(i++, application.getCollateral1());
        statement.setFloat(i++, application.getPrice1());
        statement.setString(i++, application.getCollateral2());
        statement.setFloat(i++, application.getPrice2());
        statement.setString(i++, application.getCollateral3());
        statement.setFloat(i++, application.getPrice3());
        statement.setFloat(i++, application.getRequestedMortgageAmount());
        statement.setDate(i++, Date.valueOf(application.getDate()));
        statement.setString(i++, application.getDuration());
        statement.setString(i++, application.getReason());
    }
    
    static ApplicationForm resultToModel(ResultSet resultSet) throws SQLException
    {
        int i = 1;
        var application = new ApplicationForm();
        application.setC_id(resultSet.getInt(i++));
        application.setM_id(resultSet.getInt(i++));
        application.setGrossIncome(resultSet.getFloat(i++));
        application.setWillRetired(resultSet.getBoolean(i++));
        application.setHavePartner(resultSet.getBoolean(i++));
        application.setPartnerGrossIncome(resultSet.getFloat(i++));
        application.setWillPartnerRetire(resultSet.getBoolean(i++));
        application.setHaveStudentLoan(resultSet.getBoolean(i++));
        application.setStudentDebt(resultSet.getFloat(i++));
        application.setHaveSpousalMaintenance(resultSet.getBoolean(i++));
        application.setMonthlyAlimony(resultSet.getFloat(i++));
        application.setHaveOtherDebt(resultSet.getBoolean(i++));
        application.setOtherDebtInfo(resultSet.getString(i++));
        application.setOtherDebtAmount(resultSet.getFloat(i++));
        application.setHaveCredits(resultSet.getBoolean(i++));
        application.setCreditInfo(resultSet.getString(i++));
        application.setCreditAmount(resultSet.getFloat(i++));
        application.setBankAmount(resultSet.getFloat(i++));
        application.setCollateral1(resultSet.getString(i++));
        application.setPrice1(resultSet.getFloat(i++));
        application.setCollateral2(resultSet.getString(i++));
        application.setPrice2(resultSet.getFloat(i++));
        application.setCollateral3(resultSet.getString(i++));
        application.setPrice3(resultSet.getFloat(i++));
        application.setRequestedMortgageAmount(resultSet.getFloat(i++));
        application.setDate(resultSet.getString(i++));
        application.setDuration(resultSet.getString(i++));
        application.setReason(resultSet.getString(i++));
        
        return application;
    }

    private final String INSERT_QUERY
        = "INSERT INTO application \n" +
        "(c_id, m_id, grossIncome, willRetired, havePartner, partnerGrossIncome, willPartnerRetire, haveStudentLoan, studentDebt, haveSpousalMaintenance, monthlyAlimony, haveOtherDebt, otherDebtInfo, otherDebtAmount, haveCredits, creditInfo, creditAmount, bankAmount, collateral1, price1, collateral2, price2, collateral3, price3, requestedMortgageAmount, date, duration, reason)\n" +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

	public int createApplication(ApplicationForm application)
        throws DaoInternalServerException
    {
        var date = getDateString(new java.util.Date());
        application.setDate(date);
		int newMortgageID = this.createMortgage(application.getC_id());
		this.createProcessState(newMortgageID);
        transaction.interestOfferDao().createInterestOffer(newMortgageID, application.getDuration());
		this.createMortgageCheck(newMortgageID);
		this.createStateTimestamp(newMortgageID);
        application.setM_id(newMortgageID);
        
        try {
            var statement = connection.prepareStatement(INSERT_QUERY);
            this.modelToStatement(statement, application);
            statement.execute();
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
        return newMortgageID;
	}
	//create state timestamp
	public void createStateTimestamp(int mortgageID)
      throws DaoInternalServerException
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String date = "'" + sdf.format(timestamp) + "'";

            String query = "INSERT INTO state_timestamp(m_id, state, started_time, 	end_time) "
                    + " VALUES (?, 'Application'," + date +", null)";
            var st = connection.prepareStatement(query);
            st.setInt(1, mortgageID);

            st.execute();
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
	}
	private String getDateString(java.util.Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    
	
	//create mortgage check
	public void createMortgageCheck(int mortgageID) {
        try {
            String query = "INSERT INTO mortgage_check(m_id, failedExplannation, isIncomeAccepted, isCollateralAccepted, isFiniancialObligationsAccepted) "
                    + " VALUES (?, null, null, null, null)";
            var st = connection.prepareStatement(query);
            st.setInt(1, mortgageID);
            st.execute();
        } catch (Exception e){
            e.printStackTrace();
        }
	}
	
	//create the first process state : ApplicationForm - Processing
	private void createProcessState(int mortgageID)
        throws DaoInternalServerException
    {
        try {
            String query = "INSERT INTO process_state(m_id, state, status) VALUES (?, 'Application', 'Processing')";
            var st = connection.prepareStatement(query);
            st.setInt(1, mortgageID);
            st.execute();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
	}
	
	//create MortgageRequest for new application
	public int createMortgage(int customerID)
            throws DaoInternalServerException
    {
        //TODO assign staffID correctly later
        int staffID = 3;
		try {
            var statement = connection.prepareStatement(
                "INSERT INTO mortgage_request (c_id, s_id) VALUES (?, ?) RETURNING m_id"
            );
            statement.setLong(1, customerID);
            statement.setLong(2, staffID);
            var result = statement.executeQuery();
            if (!result.next()){
                new DaoInternalServerException("Unable to get id of inserted mortgage");
                throw new RuntimeException("returning not working");
            }
            return result.getInt(1);
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
	}
	
	public ApplicationForm getApplicationFormForMortgage(int mid)
        throws DaoInternalServerException
    {
        try {
            
            String query = "SELECT * " +
                    "FROM application a"
                    + " WHERE a.m_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, mid);
            ResultSet resultSet = st.executeQuery();
            
            if (resultSet.next()) {
            	return resultToModel(resultSet);   
            } else {
                return null;
            }
        } catch (SQLException e) {
        	throw new DaoInternalServerException(e);
        }
	}
	
	public List<ApplicationForm> getAllApplicationForms() throws DaoInternalServerException {

		List<ApplicationForm> applicationformList = new ArrayList<>();
		
        try {
            Statement st = connection.createStatement();
            String query = "SELECT * " +
                    "FROM application";
            ResultSet resultSet = st.executeQuery(query);

            while (resultSet.next()) {
                applicationformList.add(resultToModel(resultSet));
            }
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
        return applicationformList;
	}
}
