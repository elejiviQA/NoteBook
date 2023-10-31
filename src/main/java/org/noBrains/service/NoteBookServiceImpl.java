package org.noBrains.service;

import org.noBrains.dao.NoteBookDao;
import org.noBrains.dao.NoteBookDaoImpl;

public class NoteBookServiceImpl implements NoteBookService {
    NoteBookDao noteBookDao = new NoteBookDaoImpl();

    @Override
    public void getAllCommands() {
        noteBookDao.getAllCommands();
    }

    @Override
    public void createNewNote() {
        noteBookDao.createNewNote();
    }

    @Override
    public void getAllNotes() {
        noteBookDao.getAllNotes();
    }

    @Override
    public void removeNoteById() {
        noteBookDao.removeNoteById();
    }

    @Override
    public void exportNotesToFile() {
        noteBookDao.exportNotesToFile();
    }

    @Override
    public void exit() {
        noteBookDao.exit();
    }
}
