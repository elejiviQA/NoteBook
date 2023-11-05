package org.noBrains.model;

import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;
@Log
public class NoteBook {
    private Long counter = 0L;
    private final List<Note> notes = new ArrayList<>();

    private Long genNewId() {
        return ++counter;
    }

    public Long getNewId() {
        return genNewId();
    }

    public List<Note> getAllNotes() {
        return notes;
    }

    public void setNote(Note note) {
        notes.add(note);
    }

}
