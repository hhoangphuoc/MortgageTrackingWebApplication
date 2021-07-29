package dao.contracts;


import dao.exceptions.DaoInternalServerException;
import model.ServiceProvider;

public interface IServiceProviderDao {
    ServiceProvider getBySId(int s_id) throws DaoInternalServerException;
    void insert(ServiceProvider serviceProvider) throws DaoInternalServerException;
}
