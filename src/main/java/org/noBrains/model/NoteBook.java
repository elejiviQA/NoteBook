package org.noBrains.model;

import lombok.extern.java.Log;
import org.noBrains.dao.NoteDaoImpl;
import org.noBrains.service.NoteService;
import org.noBrains.service.NoteServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Log
public class NoteBook {
    private Long counter = 0L;
    private final List<Note> notes = new ArrayList<>();

    public static void launch() {
        NoteService noteService = new NoteServiceImpl(new NoteDaoImpl(new NoteBook()));
        log.info("Это Ваша записная книжка. Вот список доступных команд: help, note-new, note-list, note-remove, note-export, exit.");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine().strip();
            try {
                switch (line) {
                    case "help" -> noteService.getAllCommands();
                    case "note-new" -> noteService.createNote();
                    case "note-list" -> noteService.getNotes();
                    case "note-remove" -> noteService.removeNoteById();
                    case "note-export" -> noteService.exportNotesToFile();
                    case "exit" -> noteService.exit();
                    default -> log.warning("команда не найдена");
                }
            } catch (Exception e) {
                log.warning(e.getMessage());
            }

        }
    }

    private Long genNewId() {
        return ++counter;
    }

    public Long getNewId() {
        return genNewId();
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNote(Note note) {
        notes.add(note);
    }
}
