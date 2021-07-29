package dao.exceptions;

public class DaoFailedCommitException extends  DaoInternalServerException {
//    public DaoFailedCommitException(String msg) {
//        super(msg);
//    }

    public DaoFailedCommitException(Throwable cause) {
        super(cause);
    }
}
