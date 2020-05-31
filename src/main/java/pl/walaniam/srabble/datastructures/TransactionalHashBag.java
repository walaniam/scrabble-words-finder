package pl.walaniam.srabble.datastructures;

import java.util.LinkedList;
import java.util.List;

public class TransactionalHashBag<T> extends HashBag<T> implements TransactionAware {

    private boolean inTransaction = false;
        
    /**
     * Keeps duplicated elements while transaction is opened
     */
    private final List<T> duplicatedElements = new LinkedList<T>();
    
    /**
     * {@inheritDoc}
     */
    public void beginTransaction() {
        if (inTransaction) {
            throw new IllegalStateException("Transaction already started");
        }
        duplicatedElements.clear();
        inTransaction = true;
    }
    
    /**
     * {@inheritDoc}
     */
    public void commitTransaction() {
        if (!inTransaction) {
            throw new IllegalStateException("Not in transaction");
        }
        
        for (T t : duplicatedElements) {
            addWithDuplicationAllowed(t);
            increaseSize();
        }
        
        duplicatedElements.clear();
        inTransaction = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(T o) {
        
        Object previous = getBackingMap().put(o.hashCode(), o);
        
        if (previous != null) {
            duplicatedElements.add((T) previous);
        }
        increaseSize();
    }
    
}
