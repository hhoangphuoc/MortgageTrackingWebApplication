package database;

public interface FuncTrows<A, T, E extends Throwable> {
    T call(A arg) throws E;
}
