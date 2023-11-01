package org.noBrains.service;

import lombok.extern.java.Log;
import org.noBrains.dao.NoteDao;
import org.noBrains.model.Note;

import java.util.List;

@Log
public class NoteServiceImpl implements NoteService {

    private final NoteDao noteDao;

    public NoteServiceImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public String getAllCommands() {
        return noteDao.getAllCommands();
    }

    @Override
    public Note createNewNote() throws Exception {
        return noteDao.createNewNote();
    }

    @Override
    public List<Note> getNotes() throws Exception {
        return noteDao.getNotes();
    }

    @Override
    public boolean removeNoteById() throws Exception {
        return noteDao.removeNoteById();
    }

    @Override
    public boolean exportNotesToFile() throws Exception {
        return noteDao.exportNotesToFile();
    }

    @Override
    public void exit() {
        noteDao.exit();
    }
}
