package org.noBrains.dao;

public interface NoteBookDao {
    void getAllCommands();
    void createNewNote();
    void getAllNotes();
    void removeNoteById();
    void exportNotesToFile();
    void exit();
}
