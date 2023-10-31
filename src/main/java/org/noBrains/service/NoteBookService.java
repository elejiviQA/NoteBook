package org.noBrains.service;

public interface NoteBookService {
    void getAllCommands();
    void createNewNote();
    void getAllNotes();
    void removeNoteById();
    void exportNotesToFile();
    void exit();
}
