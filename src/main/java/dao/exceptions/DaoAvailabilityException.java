package dao.exceptions;

public class DaoAvailabilityException extends DaoInternalServerException {
    public DaoAvailabilityException(String msg){
        super(msg);
    }
    public DaoAvailabilityException(Throwable cause) {
        super(cause);
    }
}
