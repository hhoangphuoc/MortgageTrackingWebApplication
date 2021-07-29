package dao.contracts;

import dao.exceptions.DaoInternalServerException;
import model.ProcessState;

public interface IProcessStateDao {
    ProcessState getProcessState(int mortgageID) throws DaoInternalServerException;
    void updateProcessState(ProcessState processState) throws DaoInternalServerException;
}
