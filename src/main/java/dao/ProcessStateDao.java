package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.contracts.IProcessStateDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.ProcessState;

public class ProcessStateDao extends BaseDao implements IProcessStateDao {

    public ProcessStateDao(Transaction transaction) {
        super(transaction);
    }

    public ProcessState getProcessState(int mortgageID)
        throws DaoInternalServerException
    {
        try {
            String query = "SELECT ps.m_id, ps.state, ps.status " +
                    "FROM process_state ps "
                    + "WHERE ps.m_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, mortgageID);
            ResultSet resultSet = st.executeQuery();
            if (!resultSet.next()) {
                return null;
            }
            String state = resultSet.getString(2);
            String status = resultSet.getString(3);

            return new ProcessState(mortgageID, state, status);
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }

    public void updateProcessState(ProcessState processState)
        throws DaoInternalServerException
    {
        try {
            String query = 	"UPDATE process_state "
                    + "SET state = ?, status = ? "
                    + "WHERE m_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            st.setString(1, processState.getState());
            st.setString(2, processState.getStatus());
            st.setInt(3, processState.getMortgageID());
            st.execute();
            st.getUpdateCount();
        } catch (SQLException e) {
            throw new DaoInternalServerException(e);
        }
    }
}
