package org.noBrains.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

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
    public String getAllCommands() {
        String commands = """
                help - выводит на экран список доступных команд с их описанием
                note-new  - создать новую заметку
                note-list - выводит все заметки на экран
                note-remove - удаляет заметку
                note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла
                exit - выход из приложения
                """;
        logCallCommand("help");
        log.info(commands);
        return commands;
    }

    @Override
    public Note createNewNote() throws Exception {
        logCallCommand("note-new");
        Scanner scanner = new Scanner(System.in);
        log.info("Введите заметку");
        log.info("Добавить метки? Метки состоят из одного слова и могу содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом.");
        return buildNote(checkText(scanner.nextLine()), checkLabels(scanner.nextLine()));
    }

    @Override
    public List<Note> getNotes() throws Exception {
        logCallCommand("note-list");
        List<Note> notes = sayIfEmpty(noteBook.getNotes());
        Scanner scanner = new Scanner(System.in);
        log.info("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок");
        String line = scanner.nextLine();
        if (line.isEmpty()) {
            log.info(toString(notes));
        } else {
            notes = matches(buildLabels(checkLabels(line)));
            log.info(toString(notes));
        }
        return notes;
    }

    @Override
    public boolean removeNoteById() throws Exception {
        logCallCommand("note-remove");
        List<Note> notes = sayIfEmpty(noteBook.getNotes());
        log.info("введите id удаляемой заметки");
        Scanner scanner = new Scanner(System.in);
        boolean result = removeIfExists(notes, checkId(scanner.nextLine()));
        log.info("Заметка удалена");
        return result;
    }

    @Override
    public boolean exportNotesToFile() throws Exception {
        logCallCommand("note-export");
        List<Note> notes = sayIfEmpty(noteBook.getNotes());
        String dirName = "notes";
        checkDirectory(dirName);
        String filePath = buildFilePath(dirName);
        boolean result = writeNotesInFile(notes, filePath);
        log.info("Успешное завершение экспорта");
        return result;
    }

    @Override
    public void exit() {
        logCallCommand("exit");
        System.exit(0);
    }

    public Long checkId(String id) throws NumberFormatException {
        if (id.isEmpty()) {
            log.info("Пустое значение");
            throw new NumberFormatException("Пустое значение");
        }
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            log.info("Неверный тип данных");
            throw new NumberFormatException("Неверный тип данных");
        }
    }

    private boolean removeIfExists(List<Note> notes, Long id) throws Exception {
        if (!notes.removeIf(note -> note.getId().equals(id))) {
            log.info("Такого id не существует");
            throw new Exception("Такого id не существует");
        }
        return true;
    }

    private void checkDirectory(String dirName) throws RuntimeException {
        Path dirPath = Paths.get(dirName);
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }
        } catch (IOException e) {
            log.info("Нет возможности обратиться к директории");
            throw new RuntimeException("Нет возможности обратиться к директории");
        }
    }

    private String buildFilePath(String dirName) throws RuntimeException {
        return dirName + "/" + "notes_" + new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss").format(new Date()) + ".txt";
    }

    private boolean writeNotesInFile(List<Note> notes, String filePath) throws RuntimeException {
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
        return true;
    }

    private String checkText(String text) throws Exception {
        if (text.length() < 3) {
            log.info("Длина текста должна быть не менее 3 символов");
            throw new Exception("Длина текста должна быть не менее 3 символов");
        }
        return text;
    }

    private String checkLabels(String labels) throws Exception {
        if (!labels.replaceAll(" ", "").chars().allMatch(Character::isLetter)) {
            log.info("Заметки должны состоять только из букв");
            throw new Exception("Заметки должны состоять только из букв");
        }
        return labels;
    }

    private Note buildNote(String text, String checkedLabels) {
            Note note = new Note(noteBook, text, buildLabels(checkedLabels.toUpperCase()));
            noteBook.setNote(note);
            System.out.println(note); //убрать строку
            log.info("Заметка добавлена");
            return note;
    }

    private List<Label> buildLabels(String checkedLabels) {
        return Stream.of(checkedLabels.replaceAll("\\s+", " ").strip().split(" ")).map(Label::new).toList();
    }

    private String toString(List<Note> notes) throws Exception {
        StringBuilder sb = new StringBuilder();
        String sp = String.format("%n");
        for (Note note : sayIfEmpty(notes)) {
            List<Label> labels = note.getLabels();
            sb.append("{").append(note.getId()).append("}#{").append(note.getText()).append("}").append(sp);
            if (!labels.get(0).toString().isEmpty()) {
                labels.forEach(label -> sb.append("{").append(label).append("};"));
                sb.deleteCharAt(sb.length() - 1).append(sp).append(sp).append("===================").append(sp).append(sp);
            } else {
                sb.append(sp).append("===================").append(sp).append(sp);
            }
        }
        sb.delete(sb.length() - 23, sb.length() - 1);
        return sb.toString();
    }

    private List<Note> matches(List<Label> checkedListLabels) {
        List<Note> result = new ArrayList<>();
        List<Label> noteLabels;
        try {
            for (Note note : sayIfEmpty(noteBook.getNotes())) {
                int count = 0;
                for (Label checkedLabel : checkedListLabels) {
                    if (count == 1) {
                        break;
                    }
                    noteLabels = note.getLabels();
                    for (Label noteLabel : noteLabels) {
                        if (checkedLabel.toString().toUpperCase().equals(noteLabel.toString())) {
                            result.add(note);
                            count = 1;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private void logCallCommand(String command) {
        log.fine("вызвана команда " + command);
    }



    private List<Note> sayIfEmpty(List<Note> notes) throws Exception {
        if (notes.isEmpty()) {
            log.info("Заметок не найдено");
            throw new Exception("Заметок не найдено");
        }
        return notes;
    }
}
