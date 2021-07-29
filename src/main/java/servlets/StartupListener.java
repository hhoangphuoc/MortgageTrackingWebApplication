package servlets;

import dao.StateTimestampDao;
import dao.contracts.IStateTimestampDao;
import dao.exceptions.DaoAccessException;
import dao.transaction.ITransactionFactory;
import dao.transaction.TransactionFactory;
import database.MyPool;

import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;

import javax.ws.rs.ext.Provider;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is used to listen to application events.
 * @author mark
 */
@Provider
public class StartupListener implements ApplicationEventListener {
    private final long MONTH_IN_MILLIS = 2629800000L;
    private final long DELAY = 0;

    private Timer timer;
    final static ITransactionFactory transactionFactory = TransactionFactory.getInstance();

    public StartupListener() {
        super();
    }

    /**
     * this class is a TimerTask
     * that when run will update the
     * MortgageProcess time estimations.
     */
    class RunPredictionCalculation extends TimerTask {
        @Override
        public void run() {
           try (var transaction = transactionFactory.create()) {
               transaction.stateTimestampDao().calculatePredictions();
           } catch (Exception e) {
               e.printStackTrace();
           }

        }
    }

    /**
     * This method listens to application events.
     * @param event the event that occurred.
     */
    @Override
    public void onEvent(ApplicationEvent event) {
        switch (event.getType()) {
            case INITIALIZATION_FINISHED: {
                this.timer = new Timer();
                timer.scheduleAtFixedRate(
                    new RunPredictionCalculation(),
                    DELAY, MONTH_IN_MILLIS
                );
                Runtime.getRuntime().addShutdownHook(
                    new Thread(()->onShutdown())
                );
            } break;
            case DESTROY_FINISHED:{
                onShutdown();
            } break;
            default: {} break;
        }
    }

    private void onShutdown(){
        System.out.println("Server Shutting down...");
        this.timer.cancel();
        MyPool.getInstance().close();
        System.out.println("Server Shutdown");
    }

    /**
     * this method listens to request events.
     * @param requestEvent request event.
     * @return null.
     */
    @Override
    public RequestEventListener onRequest(RequestEvent requestEvent) {
        return null;
    }
}
