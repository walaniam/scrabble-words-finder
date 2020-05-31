package pl.walaniam.srabble;

public class DefaultWordsLoaderTestCase extends WordsLoaderTestCase {

    @Override
    protected WordsLoader newWordsLoader() {
        return new DefaultWordsLoader();
    }
}
