package org.noBrains.service;

import org.noBrains.model.Note;

import java.util.List;

public interface NoteService {

    void getAllCommands();

    Note createNote() throws Exception;

    List<Note> getAllNotesList();

    void findNotesByLabels() throws Exception;

    void removeNoteById() throws Exception;

    void exportNotesToFile() throws Exception;

    void exit();
}
