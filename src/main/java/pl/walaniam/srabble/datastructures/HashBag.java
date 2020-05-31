package pl.walaniam.srabble.datastructures;

import gnu.trove.impl.PrimeFinder;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.*;

public class HashBag<V> implements Clearable {
    
    private final Iterator<V> emptyIterator = new Iterator<V>() {

        public boolean hasNext() {
            return false;
        }

        public V next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    };
    
    /**
     * Backing map
     */
    private final TIntObjectHashMap map = new TIntObjectHashMap(PrimeFinder.nextPrime(500000));
    
    /**
     * Values count
     */
    private int size = 0;
    
    /**
     * Add object to bag
     * 
     * @param o
     */
    public void add(V o) {
        addWithDuplicationAllowed(o);
        increaseSize();
    }
     
    protected void addWithDuplicationAllowed(V o) {
        
        final int oHash = o.hashCode();
        
        Object stored = map.get(oHash);
        
        if (stored == null) {
            map.put(oHash, o);
        } else if (stored instanceof Collection) {
            ((Collection) stored).add(o);
        } else {
            List<V> values = new LinkedList<V>();
            values.add((V) stored);
            values.add(o);
            map.put(oHash, values);
        }
    }
    
    /**
     * 
     * @return size after invoking this method
     */
    protected int increaseSize() {
        return ++size;        
    }
    
    public void addAll(Collection<V> values) {
        values.forEach(this::add);
    }
    
    public Iterator<V> get(int hash) {
        
        Object stored = map.get(hash);
        
        final Iterator<V> iterator;
        if (stored instanceof Collection) {
            iterator = ((Collection) stored).iterator();
        } else if (stored != null) {
            iterator = new SingleElementIterator<>((V) stored);
        } else {
            iterator = emptyIterator;
        }
        
        return iterator;
    }

    public Iterator<V> iterator() {
        return new BagIterator<V>(map);
    }

    @Override
    public void clear() {
        map.clear();
        size = 0;
    }
    
    public int size() {
        return size;
    }
    
    /**
     * Used to access by overriding classes
     * 
     * @return
     */
    protected TIntObjectHashMap getBackingMap() {
        return map;
    }


    private static class SingleElementIterator<E> implements Iterator<E> {
        
        private E value;
        
        public SingleElementIterator(E t) {
            value = t;
        }

        @Override
        public boolean hasNext() {
            return (value != null);
        }

        @Override
        public E next() {
            if (value == null) {
                throw new NoSuchElementException();
            }
            E t = value;
            value = null;
            return t;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    private static class BagIterator<E> implements Iterator<E> {
        
        private final Object[] values;
        
        private int index = 0;
        
        private Iterator<E> internalElementIterator;
        
        public BagIterator(TIntObjectHashMap map) {
            this.values = map.values();
        }

        @Override
        public boolean hasNext() {
            return (internalElementIterator != null && internalElementIterator.hasNext()) || (index < values.length);
        }

        @Override
        public E next() {
            
            E result = null;
            
            if (internalElementIterator != null && internalElementIterator.hasNext()) {
                
                result = internalElementIterator.next();
                
            } else {
                
                Object element = values[index++];
                
                if (element instanceof Collection) {
                    
                    internalElementIterator = ((Collection) element).iterator();
                    if (internalElementIterator.hasNext()) {
                        result = internalElementIterator.next(); 
                    }
                    
                } else {
                    result = (E) element;
                }
            }
            
            if (internalElementIterator != null && !internalElementIterator.hasNext()) {
                internalElementIterator = null;
            }
            
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
