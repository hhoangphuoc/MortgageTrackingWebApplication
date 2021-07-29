package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dao.contracts.IMortgageRequestDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.MortgageRequest;
import model.MortgageStatus;
import model.ProcessState;
import org.json.JSONArray;
import org.json.JSONObject;

public class MortgageRequestDao extends BaseDao implements IMortgageRequestDao {
	public MortgageRequestDao(Transaction transaction) {
		super(transaction);
	}

	protected MortgageRequest resultToModel
			(ResultSet resultSet, int i)
			throws SQLException
	{
		var mortgageRequest = new MortgageRequest();
		mortgageRequest.setM_id(resultSet.getInt(i++));
		mortgageRequest.setC_id(resultSet.getInt(i++));
		mortgageRequest.setS_id(resultSet.getInt(i++));
		mortgageRequest.setIndex(resultSet.getInt(i++));

		return mortgageRequest;
	}

	MortgageStatus resultToMortgageStatus
			(ResultSet resultSet, int i)
			throws SQLException
	{
		MortgageRequest mortgageRequest = resultToModel(resultSet, i);
		i += MortgageRequest.size();
		String state = resultSet.getString(i++);
		String status = resultSet.getString(i++);
		ProcessState processState = new ProcessState(mortgageRequest.getM_id(), state, status);
		MortgageStatus mortgageStatus = new MortgageStatus();
		mortgageStatus.mortgageRequest = mortgageRequest;
		mortgageStatus.processState = processState;
		mortgageStatus.customerName = resultSet.getString(i++) + " " + resultSet.getString(i++);
		mortgageStatus.date = resultSet.getString(i++);
		return mortgageStatus;
	}

	private final String GET_BY_M_ID_WITH_STATUS_QUERY = "SELECT mr.m_id, mr.c_id, mr.s_id, mr.index, ps.state, ps.status, u.name, u.surname, a.date\n"
			+ "FROM mortgage_request mr, process_state ps, \"user\" u, application a\n"
			+ "WHERE mr.m_id = ps.m_id\n"
			+ "AND mr.m_id = a.m_id \n"
			+ "AND mr.c_id = u.u_id \n"
			+ "AND mr.m_id = ?";

	public MortgageStatus getByMIdWithStatus
			(int m_id)
			throws DaoInternalServerException
	{
		try {
			var statement = connection.prepareStatement(GET_BY_M_ID_WITH_STATUS_QUERY);

			statement.setInt(1, m_id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultToMortgageStatus(resultSet, 1);
			} else {
				return null;
			}
		} catch (SQLException e){
			throw new DaoInternalServerException(e);
		}
	}

	public List<MortgageRequest> getAllMortgageRequestsbyCustomer(int customerID)
		throws DaoInternalServerException
	{
		List<MortgageRequest> mortgageList = new ArrayList<MortgageRequest>();
		try {
			String query = "SELECT mr.* " +
					"FROM mortgage_request mr "
					+ "WHERE mr.c_id = ?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, customerID);
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()) {
				MortgageRequest mortgageRequest = resultToModel(resultSet, 1);
				mortgageList.add(mortgageRequest);
			}
		} catch (SQLException e) {
			throw new DaoInternalServerException(e);
		}
		return mortgageList;
	}

	public List<MortgageStatus> getAllByCustomerWithStatus(int customerId)
		throws DaoInternalServerException
	{
		List<MortgageStatus> statusList = new ArrayList<>();

		try {
			String query = "SELECT DISTINCT mr.m_id, mr.c_id, mr.s_id, mr.index, ps.state, ps.status, u.name, u.surname, a.date\n"
					+ "FROM mortgage_request mr, process_state ps, \"user\" u, application a\n"
					+ "WHERE mr.m_id = ps.m_id\n"
					+ "AND mr.m_id = a.m_id\n"
					+ "AND mr.c_id = u.u_id\n"
					+ "AND mr.c_id = ?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, customerId);
			ResultSet resultSet = st.executeQuery();
			statusList = new ArrayList<>();
			while (resultSet.next()) {
				var status = resultToMortgageStatus(resultSet, 1);

				statusList.add(status);
			}
		} catch (SQLException e) {
			throw new DaoInternalServerException(e);
		}
		return statusList;
	}

	public List<MortgageRequest> getAllMortgageRequestsbySP(int spID) throws DaoInternalServerException {
		List<MortgageRequest> mortgageList = new ArrayList<MortgageRequest>();
		try {
			String query = "SELECT mr.* " +
					"FROM mortgage_request mr "
					+ "WHERE mr.s_id = ?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, spID);
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()) {
				var mortgageRequest = resultToModel(resultSet, 1);
				mortgageList.add(mortgageRequest);
			}
		} catch (SQLException e) {
			throw new DaoInternalServerException(e);
		}
		return mortgageList;
	}

	/**
	 * Used to fetch all data used
	 * in the service provider table of mortgages.
	 * @param spId the id of the service provider.
	 * @return an Array of json objects containing data on each mortgage.
	 */
	public JSONArray getMortgageData(int spId) throws DaoInternalServerException {
		JSONArray array = new JSONArray();

		try {
			String query = "SELECT mr.*, u.name, u.surname, a.date, ps.state, ps.status "
					+ "FROM mortgage_request mr "
					+ "LEFT JOIN process_state ps ON mr.m_id = ps.m_id "
					+ "LEFT JOIN \"user\" u ON mr.c_id = u.u_id "
					+ "LEFT JOIN application a ON mr.m_id = a.m_id "
					+ "WHERE mr.s_id = ?";

			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, spId);
			ResultSet resultSet;
			resultSet = st.executeQuery();
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			int columnCount = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				JSONObject joinedDataObject = new JSONObject();

				for (int i = 1; i <= columnCount; i++) {
					String name = resultSetMetaData.getColumnName(i);
					joinedDataObject.accumulate(name, resultSet.getObject(i));
				}

				array.put(joinedDataObject);
			}
		} catch(SQLException e) {
			throw new DaoInternalServerException(e);
		}

		return array;
	}

	public void deleteMortgageRequest(int mortgageID)
		throws DaoInternalServerException
	{
		try {
			String query = "DELETE FROM mortgage_request mr "
					+ "WHERE mr.m_id = ?";
			PreparedStatement st = connection.prepareStatement(query);
			st.setInt(1, mortgageID);
			st.executeQuery();
		} catch (SQLException e) {
		}
	}
}
