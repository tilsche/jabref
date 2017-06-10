package org.jabref.logic.sharelatex.ottypes;

public class Text {

    private final StringBuilder sb;

    public Text(String text) {
        sb = new StringBuilder(text);
    }

    public Text() {
        sb = new StringBuilder();
    }

    public void insert(int position, String text) {

        sb.insert(position, text);

    }

    public void delete(int position, String text) {
        sb.delete(position, position + text.length());
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
