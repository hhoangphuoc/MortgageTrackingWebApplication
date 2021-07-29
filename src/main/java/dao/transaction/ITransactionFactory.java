package dao.transaction;

import dao.exceptions.DaoAvailabilityException;

public interface ITransactionFactory {
    ITransaction create() throws DaoAvailabilityException;
}
