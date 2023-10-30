package org.noBrains.service;

import org.noBrains.dao.NoteBookDao;
import org.noBrains.dao.NoteBookDaoImpl;

public class NoteBookServiceImpl implements NoteBookService{
    NoteBookDao noteBookDao = new NoteBookDaoImpl();

    @Override
    public void help() {
        noteBookDao.help();
    }

    @Override
    public void createNote() {
        noteBookDao.createNote();
    }

    @Override
    public void getNotes() {
        noteBookDao.getNotes();
    }

    @Override
    public void removeNote() {

    }

    @Override
    public void saveNotes() {

    }

    @Override
    public void exit() {

    }
}
