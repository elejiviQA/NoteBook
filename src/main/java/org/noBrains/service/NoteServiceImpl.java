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
    public Note createNote() throws Exception {
        return noteDao.createNote();
    }

    @Override
    public List<Note> getNotes() throws Exception {
        return noteDao.getNotes();
    }

    @Override
    public void removeNoteById() throws Exception {
        noteDao.removeNoteById();
    }

    @Override
    public void exportNotesToFile() throws Exception {
        noteDao.exportNotesToFile();
    }

    @Override
    public void exit() {
        noteDao.exit();
    }
}
