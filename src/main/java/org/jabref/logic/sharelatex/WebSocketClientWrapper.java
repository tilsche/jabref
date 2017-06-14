package org.jabref.logic.sharelatex;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CyclicBarrier;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.Session;

import org.jabref.logic.exporter.BibtexDatabaseWriter;
import org.jabref.logic.exporter.SaveException;
import org.jabref.logic.exporter.SavePreferences;
import org.jabref.logic.exporter.StringSaveSession;
import org.jabref.model.database.BibDatabaseContext;
import org.jabref.model.database.event.BibDatabaseContextChangedEvent;

import com.google.common.eventbus.Subscribe;
import org.glassfish.tyrus.client.ClientManager;

public class WebSocketClientWrapper {

    private Session session;
    private BibDatabaseContext oldDb;
    private BibDatabaseContext newDb;

    public void createAndConnect(String channel, String projectId, BibDatabaseContext database) {

        oldDb = database;

        CyclicBarrier barrier = new CyclicBarrier(1);
        try {

            final ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
                    .preferredSubprotocols(Arrays.asList("mqttt")).build();
            ClientManager client = ClientManager.createClient();

            this.session = client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    session.addMessageHandler(String.class, (Whole<String>) message -> {
                        System.out.println("Received message: " + message);

                        if (message.contains("@book")) {
                            System.out.println("Message could be an entry ");

                        }
                        if (message.contains("otUpdateApplied") && message.contains("5936d96b1bd5906b0082f53e")) {
                            String documentId = "5936d96b1bd5906b0082f53e";

                            try {
                                leaveDocument(documentId);
                                Thread.sleep(200);
                                joinDoc(documentId);
                                Thread.sleep(200);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                    });
                }
            }, cec, new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));

            Thread.sleep(200);

            joinProject("5936d96b1bd5906b0082f53c");

            Thread.sleep(200);

            joinDoc("5936d96b1bd5906b0082f53e");

            Thread.sleep(200);


            //  database.getDatabase().registerListener(this);

            //TODO: Send/Receive with CountDownLatch
            //TODO: Change Dialog
            //TODO: Keep old database string which last came in + version
            //TODO: On database change event or on save event send new version
            //TODO: When new db content arrived run merge dialog
            //TODO: Find out how to best increment the numbers (see python script from vim)
            //TODO: Identfiy active database/Name of database/doc Id (partly done)
            //TODO: Switch back to anymous class to have all in one class?
            //TODO:

            //6:::1+[null,{"_id":"5909edaff31ff96200ef58dd","name":"Test","rootDoc_id":"5909edaff31ff96200ef58de","rootFolder":[{"_id":"5909edaff31ff96200ef58dc","name":"rootFolder","folders":[],"fileRefs":[{"_id":"5909edb0f31ff96200ef58e0","name":"universe.jpg"},{"_id":"59118cae98ba55690073c2a0","name":"all2.ris"}],"docs":[{"_id":"5909edaff31ff96200ef58de","name":"main.tex"},{"_id":"5909edb0f31ff96200ef58df","name":"references.bib"},{"_id":"5911801698ba55690073c29c","name":"aaaaaaaaaaaaaa.bib"}]}],"publicAccesLevel":"private","dropboxEnabled":false,"compiler":"pdflatex","description":"","spellCheckLanguage":"en","deletedByExternalDataSource":false,"deletedDocs":[],"members":[{"_id":"5912e195a303b468002eaad0","first_name":"jim","last_name":"","email":"jim@example.com","privileges":"readAndWrite","signUpDate":"2017-05-10T09:47:01.325Z"}],"invites":[],"owner":{"_id":"5909ed80761dc10a01f7abc0","first_name":"joe","last_name":"","email":"joe@example.com","privileges":"owner","signUpDate":"2017-05-03T14:47:28.665Z"},"features":{"trackChanges":true,"references":true,"templates":true,"compileGroup":"standard","compileTimeout":180,"github":false,"dropbox":true,"versioning":true,"collaborators":-1,"trackChangesVisible":false}},"owner",2]
            //Finden der ID mit der bib die der active database entspricht
            //idee: EntrySet stream

            //5:::{"name":"otUpdateApplied","args":[{"doc":"5909edb0f31ff96200ef58df","op":[{"d":"nov","p":223},{"i":"December","p":223}],"v":8,"meta":{"type":"external","source":"upload","user_id":"5912e195a303b468002eaad0","ts":1496736413706}}]}

            // Die ersten x-Zeichen sind immer code + ::
            // danach kommt das argument und wichtig die doc id
            //die doc id muss ich abgleichen
            //am anfang kommt aber ja erst noch das

            /*   client.connectToServer(new Endpoint() {

                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    System.out.println("On Open and is Open " + session.isOpen());

                    session.addMessageHandler(String.class, (Whole<String>) message -> {
                        System.out.println("Received message: " + message);

                    });

                    try {
                        session.getBasicRemote().sendText(
                                "5:1+::{\"name\":\"joinProject\",\"args\":[{\"project_id\":\"5909edaff31ff96200ef58dd\"}]}");
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("Sent");

                }
            }, cec, new URI("ws://192.168.1.248/socket.io/1/websocket/" + channel));        */
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void joinProject(String projectId) throws IOException {
        session.getBasicRemote().sendText(
                "5:1+::{\"name\":\"joinProject\",\"args\":[{\"project_id\":\"" + projectId + "\"}]}");
    }

    public void joinDoc(String documentId) throws IOException {
        session.getBasicRemote().sendText("5:7+::{\"name\":\"joinDoc\",\"args\":[\"" + documentId + "\"]}");
    }

    public void leaveDocument(String documentId) throws IOException {
        session.getBasicRemote().sendText("5:6+::{\"name\":\"leaveDoc\",\"args\":[\"" + documentId + "\"]}");

    }

    public void updateAsDeleteAndInsert(String docId, int position, int version, String oldContent, String newContent)
            throws IOException {
        ShareLatexJsonMessage message = new ShareLatexJsonMessage();
        String str = message.createDeleteInsertMessage(docId, position, version, oldContent, newContent);
        System.out.println("Send new update Message" + str);

        session.getBasicRemote()
                .sendText("5:8+::" + str);
    }

    @Subscribe
    public synchronized void listen(BibDatabaseContextChangedEvent event)
            throws SaveException {

        System.out.println("Event called" + event.getClass());
        BibtexDatabaseWriter<StringSaveSession> databaseWriter = new BibtexDatabaseWriter<>(StringSaveSession::new);
        StringSaveSession saveSession = databaseWriter.saveDatabase(oldDb, new SavePreferences());
        String updatedcontent = saveSession.getStringValue();

        System.out.println("OldConten " + updatedcontent);

        //TODO: We need to create a new event or add some parameters

        // return saveSession.getStringValue();

    }
}
