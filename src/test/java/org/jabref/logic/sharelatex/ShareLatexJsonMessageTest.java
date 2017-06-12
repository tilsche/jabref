package org.jabref.logic.sharelatex;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShareLatexJsonMessageTest {

    @Test
    public void testcreateDeleteInsertMessage() {
        String expected = "{\"name\":\"applyOtUpdate\",\"args\":[\"5936d96b1bd5906b0082f53e\",{\"doc\":\"5936d96b1bd5906b0082f53e\",\"op\":[{\"p\":0,\"d\":\"ToDelete \"},{\"p\":0,\"i\":\" To Insert\"}],\"v\":68}]}";
        ShareLatexJsonMessage message = new ShareLatexJsonMessage();

        String result = message.createDeleteInsertMessage("5936d96b1bd5906b0082f53e", 0, 68, "ToDelete ", " To Insert");
        assertEquals(expected, result);
    }

}
