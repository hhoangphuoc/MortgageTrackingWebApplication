package fillDb;

import java.io.IOException;
import java.sql.SQLException;

import dao.exceptions.DaoAvailabilityException;
import dao.exceptions.DaoFailedCommitException;
import dao.transaction.TransactionFactory;
import dao.transaction.Transaction;
import database.ConnectionFailed;
import file.Resources;
import model.User;

public class fillUser {
    static String dropQuery = "DROP SCHEMA IF EXISTS track_and_trace_db CASCADE; CREATE SCHEMA track_and_trace_db; SET search_path = track_and_trace_db;";


    static User[] users = {
        new User("bram@otte.biz", "password", "Bram", "Otte", false),
        new User("hayel@otte.biz", "password", "Hayel", "Akel", false),
        new User("staff@topicus.nl", "admin", "Tom", "Otte", true),
    };

    public static void main(String[] args) throws ClassNotFoundException, SQLException, RuntimeException, ConnectionFailed, IOException, DaoAvailabilityException, DaoFailedCommitException {
        var resources = Resources.getInstance();
        var createQuery = resources.readText("pgsql/CreateSchema.pgsql");
        // var fillQuery = resources.readText("pgsql/Inserts.pgsql");

        TransactionFactory factory = TransactionFactory.getInstance();
        try (Transaction transaction = factory.create()) {
            var connection = transaction.getConnection();
            var statement = connection.createStatement();
            statement.execute(dropQuery);
            statement.execute(createQuery);
            // statement.execute(fillQuery);

            var userDao = transaction.userDao();
            for (var user: users){
                try {
                    userDao.save(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(user.getId());
            }
        }
    }
}
