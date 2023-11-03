package org.noBrains.dao;

import org.noBrains.model.Label;
import org.noBrains.model.Note;

import java.nio.file.Path;
import java.util.List;

public interface NoteDao {

    Note createNote(String checkedText, List<Label> labels);

    List<Note> getAllNotesList();

    List<Note> findNotesByLabels(List<Label> labels) throws Exception;

    void removeNoteById(Long id) throws Exception;

    void exportNotesToFile(String dirName, Path filePath) throws Exception;
}
