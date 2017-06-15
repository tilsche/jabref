package org.jabref.logic.util.io;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jabref.gui.filelist.FileListTableModel;
import org.jabref.gui.undo.UndoableFieldChange;
import org.jabref.model.entry.BibEntry;
import org.jabref.model.entry.FieldName;

public interface FileFinder {

    /**
     * Finds all files in the given directories that are probably associated with the given entries and have one of the
     * passed extensions.
     *
     * @param entries     The entries to search for.
     * @param directories The root directories to search.
     * @param extensions  The extensions that are acceptable.
     */
    Map<BibEntry, List<Path>> findAssociatedFiles(List<BibEntry> entries, List<Path> directories, List<String> extensions);

    default List<Path> findAssociatedFiles(BibEntry entry, List<Path> directories, List<String> extensions) {
        Map<BibEntry, List<Path>> associatedFiles = findAssociatedFiles(Collections.singletonList(entry), directories, extensions);
        for (Map.Entry<BibEntry, List<Path>> kv : associatedFiles.entrySet())
        {
            if (!kv.getKey().getField(FieldName.FILE).isPresent() && kv.getValue().size() > 0)
            {
                String newVal = ":" + kv.getValue().get(0).toString() + ":PDF:";
                kv.getKey().setField(FieldName.FILE, newVal);
            }
        }
        return associatedFiles.getOrDefault(entry, Collections.emptyList());
    }
}
