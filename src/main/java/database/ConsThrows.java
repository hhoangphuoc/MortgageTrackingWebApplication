package database;

public interface ConsThrows <A, E extends Throwable> {
    void call(A arg) throws E;
}

