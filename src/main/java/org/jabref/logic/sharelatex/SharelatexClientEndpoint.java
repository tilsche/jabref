package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.jabref.Globals;
import org.jabref.logic.importer.ParseException;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.entry.BibEntry;

@ClientEndpoint
public class SharelatexClientEndpoint {

    private BibDatabaseContext database;

    private final ShareLatexParser parser = new ShareLatexParser();

    @OnMessage
    public void processMessageFromServer(String message, Session session)
            throws IOException, InterruptedException, ParseException {
        //if it as a return value it is send right back to the server
        System.out.println("Message came from the server ! " + message);

        if (message.contains("@book")) {

            List<BibEntry> entries = parser.parseBibEntryFromJsonArray(parser.parseFirstPartOfJson(message),
                    Globals.prefs.getImportFormatPreferences());

            System.out.println(entries);
            database.getDatabase().insertEntries(entries);
            //We have a bib document
            //Inserts the new entry
            //TODO: Check for duplication

        }
        if (message.contains("otUpdateApplied") && message.contains("5936d96b1bd5906b0082f53e")) {
            String documentId = "5936d96b1bd5906b0082f53e";
            //We got changes at our doc
            //leave doc and rejoin doc


            session.getBasicRemote().sendText("5:6+::{\"name\":\"leaveDoc\",\"args\":[\"" + documentId + "\"]}");
            Thread.sleep(200);
            session.getBasicRemote().sendText("5:7+::{\"name\":\"joinDoc\",\"args\":[\"" + documentId + "\"]}");
            Thread.sleep(200);
            //parse json array, 2. entry
            //bib database insert entry

        }

    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Session opened");

    }

    public BibDatabaseContext getDatabase() {
        return database;
    }

    public void setDatabase(BibDatabaseContext database) {
        this.database = database;
    }

}
