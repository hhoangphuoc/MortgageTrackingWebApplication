package unit.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.ClosingStack;

public class TestClosingStack {
    ClosingStack<CTest> stack;

    @BeforeEach
    void BeforeEach(){
        stack = new TestStack<>();
    }
    @Test
    void testPush(){
        stack.push(new CTest());
        assertEquals(1, stack.size());
    }
    @Test
    void testPushPop(){
        var expected = new CTest();
        stack.push(expected);
        var actual = stack.pop();
        assertEquals(expected, actual);
    }

    @Test
    void Push2Pop2(){
        var e1 = new CTest();
        var e2 = new CTest();
        stack.push(e1);
        stack.push(e2);
        var a2 = stack.pop();
        var a1 = stack.pop();

        assertEquals(e1, a1);
        assertEquals(e2, a2);
    }

    @Test
    void pushClose_BeforeStart(){
        var start = Instant.now();
        var el = new CTest();
        stack.push(el);
        
        assertFalse(el.isClosed());
        assertEquals(1, stack.size());

        stack.closeBefore(start);

        assertFalse(el.isClosed());
        assertEquals(1, stack.size());
    }

    @Test
    void pushCloseBeforeAfterPush(){
        var el = new CTest();
        
        stack.push(el);
        assertFalse(el.isClosed());
        assertEquals(1, stack.size());

        var afterPush = Instant.now();

        stack.closeBefore(afterPush);

        assertTrue(el.isClosed());
        assertEquals(0, stack.size());
    }
    @Test
    void push2CloseBefore2(){
        var el1 = new CTest();
        var el2 = new CTest();
        
        stack.push(el1);
        var t2 = Instant.now();
        stack.push(el2);
        assertEquals(2, stack.size());
        assertFalse(el1.isClosed());
        assertFalse(el2.isClosed());
        
        stack.closeBefore(t2);
        
        assertEquals(1, stack.size());
        assertTrue(el1.isClosed());
        assertFalse(el2.isClosed());
    }
    @Test
    void push2CloseBefore3(){
        var el1 = new CTest();
        var el2 = new CTest();
        
        stack.push(el1);
        stack.push(el2);
        var t2 = Instant.now();
        assertEquals(2, stack.size());
        assertFalse(el1.isClosed());
        assertFalse(el2.isClosed());
        
        stack.closeBefore(t2);
        
        assertEquals(0, stack.size());
        assertTrue(el1.isClosed());
        assertTrue(el2.isClosed());
    }
    private static class TestStack<T extends AutoCloseable> extends ClosingStack<T> {
        @Override
        public void push(T item) {
            try {
                Thread.sleep(1);
            } catch(InterruptedException e){
                throw new RuntimeException(e);
            }
            super.push(item);
            try {
                Thread.sleep(1);
            } catch(InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
    
    private static class CTest implements AutoCloseable{
        boolean closed = false;
        @Override
        public void close() throws Exception {
            closed = true;
        }
        public boolean isClosed(){
            return closed;
        }
    }
}


