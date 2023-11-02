package org.noBrains.service;

import org.noBrains.model.Label;
import org.noBrains.model.Note;

import java.util.List;

public interface NoteService {
    String getAllCommands();

    Note createNote() throws Exception;

    List<Note> findNotesByLabels() throws Exception;
    List<Note> getAllNotesList();

    void removeNoteById() throws Exception;

    void exportNotesToFile() throws Exception;

    void exit();
}
