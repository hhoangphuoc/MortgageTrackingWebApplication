package database;

import java.time.Instant;
import java.util.LinkedList;

public class ClosingStack<T extends AutoCloseable> {
    private LinkedList<T> items = new LinkedList<>();
    private LinkedList<Instant> pushInstants = new LinkedList<>();
    private int minItems;
    public ClosingStack(int minItems){
        this.minItems = minItems;
    }
    public ClosingStack(){
        this(0);
    }
    public int size(){
        return items.size();
    }
    public void push(T item){
        items.addLast(item);
        pushInstants.addLast(Instant.now());
    }
    public T pop(){
        pushInstants.removeLast();
        return items.removeLast();
    }
    public void closeBefore(Instant pushTime){
        while (
            pushInstants.size() > minItems &&
            pushInstants.getFirst().isBefore(pushTime)
        ){
            pushInstants.removeFirst();
            var item = items.removeFirst();
            try {
                item.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
