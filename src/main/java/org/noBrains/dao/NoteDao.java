package org.noBrains.dao;

import org.noBrains.model.Label;
import org.noBrains.model.Note;

import java.util.List;

public interface NoteDao {

    Note createNote(String checkedText, List<Label> labels);

    List<Note> findNotesByLabels(List<Label> labels) throws Exception;

    List<Note> getAllNotesList();

    void removeNoteById(Long id) throws Exception;

    void exportNotesToFile(String filePath) throws Exception;

    void exit();
}
