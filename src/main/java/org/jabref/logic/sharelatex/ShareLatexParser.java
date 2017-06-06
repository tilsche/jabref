package org.jabref.logic.sharelatex;



import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class ShareLatexParser {

    private final JsonParser parser = new JsonParser();

    public JsonArray parseDocIdFromProject(String documentToParse) {
        String jsonToRead =  documentToParse.substring(6, documentToParse.length());

        JsonArray obj = parser.parse(jsonToRead).getAsJsonArray();

        return obj;

    }
}
