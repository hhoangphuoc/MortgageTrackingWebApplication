package dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import dao.contracts.IStateTimestampDao;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoNotFoundException;
import dao.transaction.Transaction;
import dao.exceptions.DaoAccessException;

import model.MortgageStatus;
import model.Prediction;
import model.StateTimestamp;
import org.json.JSONArray;
import org.postgresql.util.PGInterval;

public class StateTimestampDao extends BaseDao implements IStateTimestampDao {
    public StateTimestampDao(Transaction transaction) {
        super(transaction);
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    public static StateTimestamp resultToModel
        (ResultSet resultSet)
        throws SQLException
    {
        int i = 1;
        var stateTimestamp = new StateTimestamp();
        stateTimestamp.setM_id(resultSet.getInt(i++));
        stateTimestamp.setState(resultSet.getString(i++));
        stateTimestamp.setStarted_time(resultSet.getString(i++));
        stateTimestamp.setEnd_time(resultSet.getString(i++));
        
        return stateTimestamp;
    }
    private final String GET_BY_M_ID_QUERY
            = "SELECT m_id, state, started_time, end_time\n" +
            "FROM state_timestamp\n" +
            "WHERE m_id = ?\n" +
            "AND state = ?\n";

    public StateTimestamp getStateTimestamp
        (int m_id, String state)
        throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(GET_BY_M_ID_QUERY);
            statement.setInt(1, m_id);
            statement.setString(2, state);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultToModel(resultSet);
            }
            return null;
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }

    public void updateStateTimestamp(String lastState, String currentState, int mortgageID)
        throws DaoInternalServerException
    {
    	try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            String date = "'" + sdf.format(new java.util.Date()) + "'";
            String updateQuery = "UPDATE state_timestamp "
                    + " SET end_time = " + date
                    + " WHERE m_id = ? "
                    + " AND state = ?";
            var st = connection.prepareStatement(updateQuery);
            st.setInt(1, mortgageID);
            st.setString(2, lastState);
            st.execute();



            date = "'" + sdf.format(new java.util.Date()) + "'";
            String insertQuery = "INSERT INTO state_timestamp(m_id, state, started_time, end_time) "
                    + " VALUES (?, ? ," + date +", null)";
            st = connection.prepareStatement(insertQuery);
            st.setInt(1, mortgageID);
            st.setString(2, currentState);
            st.execute();
		} catch (SQLException e) {
			throw new DaoInternalServerException(e);
		}
    }


    private final String GET_PREDICTION = "SELECT psd.state, ct.complete_time, psd.index FROM process_step_definition psd INNER JOIN completed_time ct ON ct.state = psd.state ORDER BY psd.index";

    /**
     * @returns a list of end date times starting at the current step
     */
    public List<String> getPredictions(int id) throws DaoInternalServerException, DaoNotFoundException {
        // get the mortgage.
        MortgageRequestDao msdao = new MortgageRequestDao(this.transaction);
        MortgageStatus ms = msdao.getByMIdWithStatus(id);

        if (ms == null) {
            throw new DaoNotFoundException("Mortgage Not found");
        }
        ArrayList<Prediction> predictions = new ArrayList<>();
        try {
            var st = this.connection.prepareStatement(GET_PREDICTION);

            ResultSet rs = st.executeQuery();

            // populate predictions
            while(rs.next()) {
                String state = rs.getString(1);
                PGInterval interval = (PGInterval) rs.getObject(2);
                int index = rs.getInt(3);
                Prediction stateTimestamp = new Prediction(state, interval, index);
                predictions.add(stateTimestamp);
            }
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }

        // get the start time of the mortgage
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        List<String> array = new ArrayList<>();

        try {
            Calendar cal = new GregorianCalendar();

            // calculate timestamps only for future states
            boolean flag = false;
            for (Prediction prediction : predictions) {
                // start after the current state.
                if (prediction.getState().equals(ms.processState.getState())) {
                    StateTimestamp applicationTime = this.getStateTimestamp(id, ms.processState.getState());

                    if (applicationTime == null) {
                        throw new DaoNotFoundException("time for first state not found.");
                    }

                    java.util.Date date = dateFormat.parse(applicationTime.getStarted_time());
                    // set time to start of current state.
                    cal.setTime(date);
                    flag = true;
                }

                if (flag) {
                    // reached the done state.
                    if (prediction.getInterval() == null) {
                        break;
                    }
                    
                    prediction.getInterval().add(cal);
                    array.add(dateFormat.format(cal.getTime()));
                }
            }
        } catch (ParseException e) {
            throw new DaoInternalServerException ("Parsing of dates failed");
        }

        return array;
    }

    private final String UPDATE_PREDICTIONS = "DELETE FROM completed_time;\n" +
            "INSERT INTO completed_time(state, complete_time)\n" +
            "(SELECT state, AVG(AGE(end_time, started_time)) as diff\n" +
            "FROM state_timestamp st GROUP BY state);";


    public void calculatePredictions() {
        System.out.println("Calculating");
        try {
            Statement stmt = this.connection.createStatement();
            stmt.executeUpdate(UPDATE_PREDICTIONS);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("DONE Calculating");
    }
}
