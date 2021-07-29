package dao.transaction;

import dao.*;
import dao.contracts.*;
import dao.exceptions.DaoAvailabilityException;
import dao.exceptions.DaoFailedCommitException;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoUnauthorizedException;
import database.ConnectionFailed;
import database.MyPool;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class Transaction implements ITransaction {
    MyPool pool;
    Connection connection;
    public Transaction(MyPool pool) throws DaoAvailabilityException {
        this.pool = pool;
        try {
            connection = pool.getConnection();
        } catch (ConnectionFailed e){
            throw new DaoAvailabilityException(e);
        }
    }
    @Override
    public void close() throws DaoFailedCommitException {
        if (this.connection == null){
            return;
        }
        try {
            pool.returnConnection(connection);
            this.connection = null;
        } catch (SQLException e){
            throw new DaoFailedCommitException(e);
        }
    }
    public Connection getConnection(){
        return  connection;
    }
    Optional<Integer> u_id = Optional.empty();
    @Override
    public int getU_id() throws DaoUnauthorizedException
    {
        if (u_id.isEmpty()){
            throw new DaoUnauthorizedException("Unable to get u_id because there is no valid auth token given");
        }
        return u_id.get();
    }

    @Override
    public void setU_id(int u_id) {
        this.u_id = Optional.of(u_id);
    }

    @Override
    public IApplicationFormDao applicationFormDao() {
        return new ApplicationFormDao(this);
    }

    @Override
    public IMortgageRequestDao mortgageRequestDao() {
        return new MortgageRequestDao(this);
    }

    @Override
    public IAuthenticationDao authenticationDao() {
        return new AuthenticationDao(this);
    }

    @Override
    public ICustomerDao customerDao() {
        return  new CustomerDao(this);
    }

    @Override
    public IDocumentRequestDao documentRequestDao() {
        return new DocumentRequestDao(this);
    }

    @Override
    public IFileDao fileDao() {
        return new FileDao(this);
    }

    @Override
    public IInterestOfferDao interestOfferDao() {
        return new InterestOfferDao(this);
    }

    @Override
    public IMortgageCheckDao mortgageCheckDao() {
        return new MortgageCheckDao(this);
    }

    @Override
    public IProcessStateDao processStateDao() {
        return new ProcessStateDao(this);
    }

    @Override
    public IServiceProviderDao serviceProviderDao() {
        return  new ServiceProviderDao(this);
    }

    @Override
    public IStateTimestampDao stateTimestampDao() {
        return new StateTimestampDao(this);
    }

    @Override
    public IUserDao userDao() {
        return new UserDao(this);
    }

    @Override
    public IChatDao chatDao() {
        return new ChatDao(this);
    }
}
