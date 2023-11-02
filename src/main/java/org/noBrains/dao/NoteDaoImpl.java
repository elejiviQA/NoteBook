package org.noBrains.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import lombok.extern.java.Log;
import org.noBrains.model.Label;
import org.noBrains.model.NoteBook;
import org.noBrains.model.Note;

@Log
public class NoteDaoImpl implements NoteDao {
    private final NoteBook noteBook;

    public NoteDaoImpl(NoteBook noteBook) {
        this.noteBook = noteBook;
    }

    @Override
    public Note createNote(String text, List<Label> labels) {
        Note note = new Note(noteBook, text, labels);
        noteBook.setNote(note);
        return note;
    }

    @Override
    public List<Note> getAllNotesList() {
        return noteBook.getAllNotes();
    }

    @Override
    public List<Note> findNotesByLabels(List<Label> labels) {
        return getMatchingNotes(labels);
    }

    private List<Note> getMatchingNotes(List<Label> checkedLabelsList) {
        if (checkedLabelsList.size() == 1 && checkedLabelsList.get(0).toString().isEmpty()) {
            return getAllNotesList();
        }
        List<Note> matchingNotes = new ArrayList<>();
        List<Label> noteLabels;
        for (Note note : getAllNotesList()) {
            int count = 0;
            for (Label checkedLabel : checkedLabelsList) {
                if (count == 1) {
                    break;
                }
                noteLabels = note.getLabels();
                for (Label noteLabel : noteLabels) {
                    if (checkedLabel.toString().toUpperCase().equals(noteLabel.toString())) {
                        matchingNotes.add(note);
                        count = 1;
                        break;
                    }
                }
            }
        }
        return matchingNotes;
    }

    @Override
    public void removeNoteById(Long id) throws Exception {
        removeIfExists(getAllNotesList(), id);
    }

    private void removeIfExists(List<Note> notes, Long id) throws Exception {
        if (!notes.removeIf(note -> note.getId().equals(id))) {
            log.info("Такого id не существует");
            throw new Exception("Такого id не существует");
        }
    }

    @Override
    public void exportNotesToFile(String filePath) {
        writeNotesInFile(getAllNotesList(), filePath);
    }

    private void writeNotesInFile(List<Note> notes, String filePath) throws RuntimeException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            notes.forEach(note -> {
                try {
                    bufferedWriter.write(note + String.format("%n"));
                } catch (IOException e) {
                    log.info("Ошибка записи");
                    throw new RuntimeException("Ошибка записи");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи");
        }
    }

    @Override
    public void exit() {
        System.exit(0);
    }
}
