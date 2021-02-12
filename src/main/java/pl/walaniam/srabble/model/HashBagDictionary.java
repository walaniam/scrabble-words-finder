package pl.walaniam.srabble.model;

import pl.walaniam.srabble.datastructures.CompactCharSequence;
import pl.walaniam.srabble.datastructures.TransactionAware;
import pl.walaniam.srabble.datastructures.TransactionalHashBag;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static pl.walaniam.srabble.util.LetterGroupingUtils.groupByStartingLetter;

/**
 * Hash bag dictionary.
 */
public class HashBagDictionary implements WordsDictionary, TransactionAware {

    private final TransactionalHashBag<CompactCharSequence> bag = new TransactionalHashBag<>();

    private Map<Character, List<CompactCharSequence>> byStartingLetter;

    @Override
    public String getFromDictionary(String word) {

        CompactCharSequence searched = new CompactCharSequence(word);

        CompactCharSequence found = null;
        Iterator<CompactCharSequence> values = bag.get(searched.hashCode());
        if (values != null) {
            while (values.hasNext()) {
                CompactCharSequence next = values.next();
                if (searched.equals(next)) {
                    found = next;
                    break;
                }
            }
        }

        return (found == null) ? null : word;
    }

    @Override
    public Collection<String> getWordsStartingWith(char letter) {

        if (byStartingLetter == null) {
            byStartingLetter = groupByStartingLetter(bag.iterator(), false, CompactCharSequence.COMPARATOR);
        }

        return CompactCharSequence.asStringCollection(byStartingLetter.get(letter));
    }

    @Override
    public void add(String word) {
        bag.add(new CompactCharSequence(word));
    }

    @Override
    public void beginTransaction() {
        bag.beginTransaction();
    }

    @Override
    public void commitTransaction() {
        bag.commitTransaction();
    }

    @Override
    public int size() {
        return bag.size();
    }
}
