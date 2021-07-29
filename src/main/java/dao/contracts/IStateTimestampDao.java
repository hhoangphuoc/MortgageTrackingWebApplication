package dao.contracts;

import dao.exceptions.DaoAccessException;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoNotFoundException;
import model.StateTimestamp;
import org.json.JSONArray;

import java.sql.SQLException;
import java.util.List;

public interface IStateTimestampDao {
    StateTimestamp getStateTimestamp(int m_id, String state) throws DaoInternalServerException;
    void updateStateTimestamp(String lastState, String currentState, int mortgageID) throws DaoInternalServerException;

    List<String> getPredictions(int mortgageID) throws DaoInternalServerException, DaoNotFoundException;

    void calculatePredictions();
}
