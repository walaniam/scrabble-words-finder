package pl.walaniam.srabble.datastructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CompactCharSequence implements ComparableCharSequence {

    public static final Comparator<CompactCharSequence> COMPARATOR = CompactCharSequence::compareTo;

    private final char[] characters;
    private int hash = 0;

    public CompactCharSequence(String str) {
        this.characters = str.toCharArray();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompactCharSequence that = (CompactCharSequence) o;
        return Arrays.equals(characters, that.characters);
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = Arrays.hashCode(characters);
        }
        return hash;
    }

    @Override
    public String toString() {
        return new String(characters);
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