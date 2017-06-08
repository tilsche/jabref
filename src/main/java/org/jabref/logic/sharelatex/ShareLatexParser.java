package org.jabref.logic.sharelatex;

import java.util.List;
import java.util.Map;

import org.jabref.logic.importer.ImportFormatPreferences;
import org.jabref.logic.importer.ParseException;
import org.jabref.logic.importer.fileformat.BibtexParser;
import org.jabref.model.entry.BibEntry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ShareLatexParser {

    private final JsonParser parser = new JsonParser();

    public JsonArray parseFirstPartOfJson(String documentToParse) {
        String jsonToRead = documentToParse.substring(6, documentToParse.length());

        JsonArray obj = parser.parse(jsonToRead).getAsJsonArray();

        return obj;

    }

    public List<BibEntry> parseBibEntryFromJsonArray(JsonArray arr, ImportFormatPreferences prefs)
            throws ParseException {
        JsonArray stringArr = arr.get(1).getAsJsonArray();
        StringBuilder builder = new StringBuilder();
        for (JsonElement elem : stringArr) {
            builder.append(elem.getAsString());
        }

        BibtexParser parser = new BibtexParser(prefs);
        return parser.parseEntries(builder.toString());

    }

    public Map<String, String> getDatabaseWithId(String json) {

        JsonObject obj = parseFirstPartOfJson(json).get(1).getAsJsonObject();
        JsonArray arr = obj.get("rootFolder").getAsJsonArray();



        return null;
    }
}
