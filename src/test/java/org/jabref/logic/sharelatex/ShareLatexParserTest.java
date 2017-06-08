package org.jabref.logic.sharelatex;

import java.util.List;
import java.util.Map;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParseException;
import org.jabref.model.entry.BibEntry;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.mockito.Answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ShareLatexParserTest {

    @Test
    public void testParseDocIdFromProject() {

        String document = "6:::1+[null,{\"_id\":\"5909edaff31ff96200ef58dd\",\"name\":\"Test\",\"rootDoc_id\":\"5909edaff31ff96200ef58de\",\"rootFolder\":[{\"_id\":\"5909edaff31ff96200ef58dc\",\"name\":\"rootFolder\",\"folders\":[],\"fileRefs\":[{\"_id\":\"5909edb0f31ff96200ef58e0\",\"name\":\"universe.jpg\"},{\"_id\":\"59118cae98ba55690073c2a0\",\"name\":\"all2.ris\"}],\"docs\":[{\"_id\":\"5909edaff31ff96200ef58de\",\"name\":\"main.tex\"},{\"_id\":\"5909edb0f31ff96200ef58df\",\"name\":\"references.bib\"},{\"_id\":\"5911801698ba55690073c29c\",\"name\":\"aaaaaaaaaaaaaa.bib\"},{\"_id\":\"59368d551bd5906b0082f53a\",\"name\":\"aaaaaaaaaaaaaa (copy 1).bib\"}]}],\"publicAccesLevel\":\"private\",\"dropboxEnabled\":false,\"compiler\":\"pdflatex\",\"description\":\"\",\"spellCheckLanguage\":\"en\",\"deletedByExternalDataSource\":false,\"deletedDocs\":[],\"members\":[{\"_id\":\"5912e195a303b468002eaad0\",\"first_name\":\"jim\",\"last_name\":\"\",\"email\":\"jim@example.com\",\"privileges\":\"readAndWrite\",\"signUpDate\":\"2017-05-10T09:47:01.325Z\"}],\"invites\":[],\"owner\":{\"_id\":\"5909ed80761dc10a01f7abc0\",\"first_name\":\"joe\",\"last_name\":\"\",\"email\":\"joe@example.com\",\"privileges\":\"owner\",\"signUpDate\":\"2017-05-03T14:47:28.665Z\"},\"features\":{\"trackChanges\":true,\"references\":true,\"templates\":true,\"compileGroup\":\"standard\",\"compileTimeout\":180,\"github\":false,\"dropbox\":true,\"versioning\":true,\"collaborators\":-1,\"trackChangesVisible\":false}},\"owner\",2]";

        ShareLatexParser parser = new ShareLatexParser();

        String withoutFirstSix = "[null,{\"_id\":\"5909edaff31ff96200ef58dd\",\"name\":\"Test\",\"rootDoc_id\":\"5909edaff31ff96200ef58de\",\"rootFolder\":[{\"_id\":\"5909edaff31ff96200ef58dc\",\"name\":\"rootFolder\",\"folders\":[],\"fileRefs\":[{\"_id\":\"5909edb0f31ff96200ef58e0\",\"name\":\"universe.jpg\"},{\"_id\":\"59118cae98ba55690073c2a0\",\"name\":\"all2.ris\"}],\"docs\":[{\"_id\":\"5909edaff31ff96200ef58de\",\"name\":\"main.tex\"},{\"_id\":\"5909edb0f31ff96200ef58df\",\"name\":\"references.bib\"},{\"_id\":\"5911801698ba55690073c29c\",\"name\":\"aaaaaaaaaaaaaa.bib\"},{\"_id\":\"59368d551bd5906b0082f53a\",\"name\":\"aaaaaaaaaaaaaa (copy 1).bib\"}]}],\"publicAccesLevel\":\"private\",\"dropboxEnabled\":false,\"compiler\":\"pdflatex\",\"description\":\"\",\"spellCheckLanguage\":\"en\",\"deletedByExternalDataSource\":false,\"deletedDocs\":[],\"members\":[{\"_id\":\"5912e195a303b468002eaad0\",\"first_name\":\"jim\",\"last_name\":\"\",\"email\":\"jim@example.com\",\"privileges\":\"readAndWrite\",\"signUpDate\":\"2017-05-10T09:47:01.325Z\"}],\"invites\":[],\"owner\":{\"_id\":\"5909ed80761dc10a01f7abc0\",\"first_name\":\"joe\",\"last_name\":\"\",\"email\":\"joe@example.com\",\"privileges\":\"owner\",\"signUpDate\":\"2017-05-03T14:47:28.665Z\"},\"features\":{\"trackChanges\":true,\"references\":true,\"templates\":true,\"compileGroup\":\"standard\",\"compileTimeout\":180,\"github\":false,\"dropbox\":true,\"versioning\":true,\"collaborators\":-1,\"trackChangesVisible\":false}},\"owner\",2]";
        JsonParser jsonParser = new JsonParser();
        JsonArray obj = jsonParser.parse(withoutFirstSix).getAsJsonArray();

        //TODO: Write new method that extracs the database name and the id of the database
        assertEquals(obj, parser.parseFirstPartOfJson(document));

    }

    @Test
    public void testParseBibEntries() throws ParseException {
        String bibTexString = "6:::7+[null,[\"@book{adams1995hitchhiker,\",\"  title={The Hitchhiker's Guide to the Galaxy},\",\"  author={Adams, D.},\",\"  isbn={9781417642595},\",\"  url={http://books.google.com/books?id=W-xMPgAACAAJ},\",\"  year={1995}\",\"  publisher={San Val}\",\"}\",\"\"],5,[],{}]";
        ShareLatexParser parser = new ShareLatexParser();

        ImportFormatPreferences prefs = mock(ImportFormatPreferences.class, Answers.RETURNS_DEEP_STUBS);
        when(prefs.getKeywordSeparator()).thenReturn(',');

        List<BibEntry> entries = parser.parseBibEntryFromJsonArray(parser.parseFirstPartOfJson(bibTexString),
                prefs);
        System.out.println(entries);
        assertFalse(entries.isEmpty());
    }

    @Test
    public void testgetDatabaseWithId() {
        String document = "6:::1+[null,{\"_id\":\"5909edaff31ff96200ef58dd\",\"name\":\"Test\",\"rootDoc_id\":\"5909edaff31ff96200ef58de\",\"rootFolder\":[{\"_id\":\"5909edaff31ff96200ef58dc\",\"name\":\"rootFolder\",\"folders\":[],\"fileRefs\":[{\"_id\":\"5909edb0f31ff96200ef58e0\",\"name\":\"universe.jpg\"},{\"_id\":\"59118cae98ba55690073c2a0\",\"name\":\"all2.ris\"}],\"docs\":[{\"_id\":\"5909edaff31ff96200ef58de\",\"name\":\"main.tex\"},{\"_id\":\"5909edb0f31ff96200ef58df\",\"name\":\"references.bib\"},{\"_id\":\"5911801698ba55690073c29c\",\"name\":\"aaaaaaaaaaaaaa.bib\"},{\"_id\":\"59368d551bd5906b0082f53a\",\"name\":\"aaaaaaaaaaaaaa (copy 1).bib\"}]}],\"publicAccesLevel\":\"private\",\"dropboxEnabled\":false,\"compiler\":\"pdflatex\",\"description\":\"\",\"spellCheckLanguage\":\"en\",\"deletedByExternalDataSource\":false,\"deletedDocs\":[],\"members\":[{\"_id\":\"5912e195a303b468002eaad0\",\"first_name\":\"jim\",\"last_name\":\"\",\"email\":\"jim@example.com\",\"privileges\":\"readAndWrite\",\"signUpDate\":\"2017-05-10T09:47:01.325Z\"}],\"invites\":[],\"owner\":{\"_id\":\"5909ed80761dc10a01f7abc0\",\"first_name\":\"joe\",\"last_name\":\"\",\"email\":\"joe@example.com\",\"privileges\":\"owner\",\"signUpDate\":\"2017-05-03T14:47:28.665Z\"},\"features\":{\"trackChanges\":true,\"references\":true,\"templates\":true,\"compileGroup\":\"standard\",\"compileTimeout\":180,\"github\":false,\"dropbox\":true,\"versioning\":true,\"collaborators\":-1,\"trackChangesVisible\":false}},\"owner\",2]";

        ShareLatexParser parser = new ShareLatexParser();
        Map<String, Object> flatMap = parser.getDatabaseWithId(document);

        flatMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

    }
}
