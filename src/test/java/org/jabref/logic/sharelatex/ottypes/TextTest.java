package org.jabref.logic.sharelatex.ottypes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testInsert() {
        Text t = new Text();
        String testInsert = "@book{adams1995hitchhiker,       \r\n" +
                "   title={The Hitchhiker's Guide to the Galaxy},\r\n" +
                "  author={Adams, D.},\r\n" +
                "  isbn={9781417642595},\r\n" +
                "  url={http://books.google.com/books?id=W-xMPgAACAAJ},\r\n" +
                "  year={1995},\r\n" +
                "  publisher={San Val}\r\n" +
                "}";

        t.insert(0, testInsert);

        assertEquals(testInsert, t.toString());
    }

    @Test
    public void mulitInsert() {

        String afterPosition = "This is an example\n" +
                "an";
        Text t = new Text();
        t.insert(0, "T");
        t.insert(1, "his is an e");
        t.insert(12, "xample");
        t.insert(18, "\nan");

        assertEquals(afterPosition, t.toString());

    }

    @Test
    public void testDelete() {

        String testDelete = "@book{adams1995hitchhiker,       \n" +
                "   title={The Hitchhiker's Guide to the Galaxy},\n" +
                "  author={Adams, D.},\n" +
                "  isbn={9781417642595},\n" +
                "  url={http://books.google.com/books?id=W-xMPgAACAAJ},\n" +
                "  year={1995},\n" +
                "  publisher={San Val}\n" +
                "}";

        String afterDelete = "@book{adams1995hitchhiker,       \n" +
                "   title={The Hitchhiker's Guide to the Galaxy},\n" +
                "  author={Adams, D.},\n" +
                "  isbn={9781417642595},\n" +
                "  url={http://books.google.com/books?id=W-xMPgAACAAJ},\n" +
                "  year={199},\n" +
                "  publisher={San Val}\n" +
                "}";

        Text t = new Text(testDelete);
        t.delete(195, "5");
        assertEquals(afterDelete, t.toString());
    }

    @Test
    public void testInsertAndDeleteTogehter() {

        String afterPosition = "This is an example\n" +
                "replaced";
        Text t = new Text();
        t.insert(0, "T");
        t.insert(1, "his is an e");
        t.insert(12, "xample");
        t.insert(18, "\nan");

        t.delete(19, "an");
        t.insert(19, "replaced");
        assertEquals(afterPosition, t.toString());
    }

}
