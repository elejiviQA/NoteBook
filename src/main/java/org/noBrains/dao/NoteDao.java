package org.noBrains.dao;

import org.noBrains.model.Note;

import java.util.List;

public interface NoteDao {
    String getAllCommands();

    Note createNote() throws Exception;

    List<Note> getNotes() throws Exception;

    void removeNoteById() throws Exception;

    void exportNotesToFile() throws Exception;

    void exit();
}
