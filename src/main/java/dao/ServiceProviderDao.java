package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.contracts.IServiceProviderDao;
import dao.exceptions.DaoInternalServerException;
import dao.transaction.Transaction;
import model.ServiceProvider;
 
public class ServiceProviderDao extends BaseDao implements IServiceProviderDao {
    public ServiceProviderDao(Transaction transaction) {
        super(transaction);
    }

    public void modelToStatement
        (PreparedStatement statement, ServiceProvider serviceProvider)
        throws SQLException
    {
        int i = 1;
        statement.setInt(i++, serviceProvider.getS_id());
    }
    protected ServiceProvider resultToModel
        (ResultSet resultSet)
        throws SQLException
    {
        var serviceProvider = new ServiceProvider();
        int i = 1;
        serviceProvider.setS_id(resultSet.getInt(i++));
        
        return serviceProvider;
    }


    private final String GET_BY_S_ID_QUERY
            = "SELECT s_id\n" +
            "FROM \"service_provider\"\n" +
            "WHERE s_id = ?\n" +
            "LIMIT 1\n";

    public ServiceProvider getBySId(int s_id) throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(GET_BY_S_ID_QUERY);
            statement.setInt(1, s_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return this.resultToModel(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }


    private final String INSERT_QUERY
            = "INSERT INTO service_provider \n" +
            "(s_id)\n" +
            "VALUES (?)\n" +
            "RETURNING s_id\n";

    public void insert(ServiceProvider serviceProvider) throws DaoInternalServerException
    {
        try {
            var statement = connection.prepareStatement(INSERT_QUERY);
            this.modelToStatement(statement, serviceProvider);
            statement.execute();
        } catch (SQLException e){
            throw new DaoInternalServerException(e);
        }
    }
  
        

}
