package pl.walaniam.srabble.datastructures;

public interface TransactionAware {

    void beginTransaction();
    
    void commitTransaction();    
}
