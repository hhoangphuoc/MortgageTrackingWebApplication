package dao.contracts;

import dao.exceptions.DaoInternalServerException;
import model.Customer;

public interface ICustomerDao {
    Customer getByCId(int c_id) throws DaoInternalServerException;
    void insert(Customer customer) throws DaoInternalServerException;
}
