package dao.transaction;

import dao.contracts.*;
import dao.exceptions.DaoFailedCommitException;
import dao.exceptions.DaoInternalServerException;
import dao.exceptions.DaoUnauthorizedException;

public interface ITransaction extends AutoCloseable {
    @Override
    void close() throws DaoFailedCommitException;

    int getU_id() throws DaoUnauthorizedException;
    void setU_id(int u_id);
    default int authenticate(String authToken) throws DaoInternalServerException, DaoUnauthorizedException {
        var id = authenticationDao().getU_id(authToken);
        if (id.isPresent()){
            setU_id(id.get());
            return id.get();
        } else {
            throw new DaoUnauthorizedException("Can't authenticate when there is no valid user token given");
        }
    }

    IApplicationFormDao applicationFormDao();
    IMortgageRequestDao mortgageRequestDao();
    IAuthenticationDao authenticationDao();
    ICustomerDao customerDao();
    IDocumentRequestDao documentRequestDao();
    IFileDao fileDao();
    IInterestOfferDao interestOfferDao();
    IMortgageCheckDao mortgageCheckDao();
    IProcessStateDao processStateDao();
    IServiceProviderDao serviceProviderDao();
    IStateTimestampDao stateTimestampDao();
    IUserDao userDao();
    IChatDao chatDao();
}
