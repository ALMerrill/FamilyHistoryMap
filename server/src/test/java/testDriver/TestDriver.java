package testDriver;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class TestDriver {
        public static void main(String[] args) {
//        org.junit.runner.JUnitCore.runClasses(
//                dataaccess.DatabaseTest.class,
//                spellcheck.URLFetcherTest.class,
//                spellcheck.WordExtractorTest.class,
//                spellcheck.DictionaryTest.class,
//                spellcheck.SpellingCheckerTest.class
//                );

            org.junit.runner.JUnitCore.main(
                    "access.AuthTokenDaoTest",
                    "access.EventDaoTest",
                    "access.PersonDaoTest",
                    "access.UserDaoTest");
        }
}
