package pl.walaniam.srabble.datastructures;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class CompactCharSequence implements ComparableCharSequence {

    private static final long serialVersionUID = 1L;
    public static final Comparator<CompactCharSequence> COMPARATOR = CompactCharSequence::compareTo;

    private final char[] characters;
    private int hash = 0;

    public CompactCharSequence(String str) {
        this.characters = str.toCharArray();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0 || end >= characters.length) {
            throw new IllegalArgumentException("Illegal range " + start + "-"
                    + end + " for sequence of length " + characters.length);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public char charAt(int index) {
        if (index >= characters.length || index < 0) {
            throw new StringIndexOutOfBoundsException("Invalid index " + index
                    + " length " + characters.length);
        }

        return characters[index];
    }

    @Override
    public int length() {
        return characters.length;
    }

    @Override
    public String toString() {
        return new String(characters);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            int off = 0;
            char val[] = characters;
            int len = characters.length;

            for (int i = 0; i < len; i++) {
                h = 31 * h + val[off++];
            }
            hash = h;
        }
        return h;
    }

    @Override
    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof CompactCharSequence) {
            CompactCharSequence that = (CompactCharSequence) anObject;
            int n = characters.length;
            if (n == that.characters.length) {
                char v1[] = characters;
                char v2[] = that.characters;
                int i = 0;
                int j = 0;
                while (n-- != 0) {
                    if (v1[i++] != v2[j++])
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(ComparableCharSequence o) {
        
        char v1[] = characters;
        char v2[] = ((CompactCharSequence) o).characters;

        int len1 = v1.length;
        int len2 = v2.length;
        int lim = Math.min(len1, len2);

        int k = 0;
        while (k < lim) {
            char c1 = v1[k];
            char c2 = v2[k];
            if (c1 != c2) {
                return c1 - c2;
            }
            k++;
        }

        return len1 - len2;
    }

    public static List<String> asStringCollection(Collection<CompactCharSequence> seq) {
        return StreamSupport.stream(seq.spliterator(), false)
                .map(CompactCharSequence::toString)
                .collect(Collectors.toList());
    }
}