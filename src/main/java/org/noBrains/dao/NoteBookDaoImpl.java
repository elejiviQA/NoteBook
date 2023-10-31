package org.noBrains.dao;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import lombok.extern.java.Log;
import org.noBrains.model.Label;
import org.noBrains.model.Note;

import static org.noBrains.model.Note.*;

@Log
public class NoteBookDaoImpl implements NoteBookDao {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void getAllCommands() {
        callCommandLog("help");
        log.info("""
                help - выводит на экран список доступных команд с их описанием
                note-new  - создать новую заметку
                note-list - выводит все заметки на экран
                note-remove - удаляет заметку
                note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла
                exit - выход из приложения
                """);
    }

    @Override
    public void createNewNote() {
        callCommandLog("note-new");
        try {
            log.info("Введите заметку");
            String text = scanner.nextLine();
            checkText(text);
            log.info("Добавить метки? Метки состоят из одного слова и могу содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом.");
            String labels = scanner.nextLine();
            checkLabels(labels);
            buildNote(text, labels);
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    @Override
    public void getAllNotes() {
        callCommandLog("note-list");
        try {
            sayIfEmpty(getNotesList());
            log.info("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок");
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                log.info(toString(getNotesList()));
            } else {
                log.info(toString(matches(buildLabels(checkLabels(line)))));
            }
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    @Override
    public void removeNoteById() {
        callCommandLog("note-remove");
        log.info("введите id удаляемой заметки");
        try {
            Long id = checkId(scanner.nextLine());
            sayIfEmpty(getNotesList()).removeIf(note -> note.getId().equals(id));
            log.info("Заметка удалена");
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    @Override
    public void exportNotesToFile() {
        callCommandLog("note-export");
        String dirName = "notes";
        String filePath = buildFilePath(dirName);
        checkDirectory(dirName);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
            writeNotesInFile(sayIfEmpty(getNotesList()), bufferedWriter);
            log.info("Успешное завершение экспорта");
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    @Override
    public void exit() {
        callCommandLog("exit");
        System.exit(0);
    }

    private Long checkId(String id) throws NumberFormatException {
        if (id.isEmpty()) {
            log.info("Пустое значение");
            throw new NumberFormatException("Пустое значение");
        }
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            log.info("Неверное значение");
            throw new NumberFormatException("Неверное значение");
        }
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

    private void writeNotesInFile(List<Note> notes, BufferedWriter bufferedWriter) throws RuntimeException {
        notes.forEach(note -> {
            try {
                bufferedWriter.write(note + String.format("%n"));
            } catch (IOException e) {
                log.info("Ошибка записи");
                throw new RuntimeException("Ошибка записи");
            }
        });
    }
    private void checkText(String text) throws Exception {
        if (text.length() < 3) {
            log.info("Длина текста должна быть не менее 3 символов");
            throw new Exception("Длина текста должна быть не менее 3 символов");
        }
    }

    private String checkLabels(String labels) throws Exception {
        if (!labels.replaceAll(" ", "").chars().allMatch(Character::isLetter)) {
            log.info("Заметки должны состоять только из букв");
            throw new Exception("Заметки должны состоять только из букв");
        }
        return labels;
    }

    private void buildNote(String text, String checkedLabels) throws Exception {
        try {
            Note note = new Note(text, buildLabels(checkedLabels.toUpperCase()));
            setNote(note);
            System.out.println(note); //убрать строку
            log.info("Заметка добавлена");
        } catch (Exception e) {
            log.info("Невозможно создать заметку");
            throw new Exception("Невозможно создать заметку");
        }
    }

    private List<Label> buildLabels(String checkedLabels) {
        return Stream.of(checkedLabels.replaceAll("\\s+", " ").strip().split(" ")).map(Label::new).toList();
    }

    private String toString(List<Note> notes) {
        StringBuilder sb = new StringBuilder();
        String sp = String.format("%n");
        try {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return sb.toString();
    }

    private List<Note> matches(List<Label> checkedListLabels) {
        List<Note> result = new ArrayList<>();
        List<Label> noteLabels;
        try {
            for (Note note : sayIfEmpty(getNotesList())) {
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

    private void callCommandLog(String command) {
        log.fine("вызвана команда " + command);
    }

    private void logError(String message) {
        log.warning(message);
    }
    private List<Note> sayIfEmpty(List<Note> notes) throws Exception {
        if (notes.isEmpty()) {
            log.info("Заметок не найдено");
            throw new Exception("Заметок не найдено");
        }
        return notes;
    }
}
