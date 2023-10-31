package org.noBrains;

import lombok.extern.java.Log;
import org.noBrains.service.NoteBookService;
import org.noBrains.service.NoteBookServiceImpl;
import java.util.Scanner;

@Log
public class Main {
    public static void main(String[] args) {
        NoteBookService noteBookService = new NoteBookServiceImpl();
        System.out.println("Это Ваша записная книжка. Вот список доступных команд: help, note-new, note-list, note-remove, note-export, exit.");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine().strip();
            switch (line) {
                case "help" -> noteBookService.getAllCommands();
                case "note-new" -> noteBookService.createNewNote();
                case "note-list" -> noteBookService.getAllNotes();
                case "note-remove" -> noteBookService.removeNoteById();
                case "note-export" -> noteBookService.exportNotesToFile();
                case "exit" -> noteBookService.exit();
                default -> log.warning("команда не найдена");
            }
        }
    }
}
