package pl.walaniam.srabble;

public class FastWordsLoaderTestCase extends WordsLoaderTestCase {

    @Override
    protected WordsLoader newWordsLoader() {
        return new FastWordsLoader();
    }
}
