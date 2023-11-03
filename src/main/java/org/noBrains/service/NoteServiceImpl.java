package org.noBrains.service;

import lombok.extern.java.Log;
import org.noBrains.dao.NoteDao;
import org.noBrains.model.Label;
import org.noBrains.model.Note;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;


@Log
public class NoteServiceImpl implements NoteService {

    private final NoteDao noteDao;

    public NoteServiceImpl(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public void getAllCommands() {
        logCallCommand("help");
        String commands = """
                help - выводит на экран список доступных команд с их описанием
                note-new  - создать новую заметку
                note-list - выводит все заметки на экран
                note-remove - удаляет заметку
                note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла
                exit - выход из приложения
                """;
        log.info(commands);
    }

    private void logCallCommand(String command) {
        log.fine("вызвана команда " + command);
    }

    @Override
    public Note createNote() throws Exception {
        logCallCommand("note-new");
        Scanner scanner = new Scanner(System.in);
        log.info("Введите заметку");
        String checkedText = checkText(scanner.nextLine());
        log.info("Добавить метки? Метки состоят из одного слова и могу содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом.");
        List<Label> checkedLabelsList = buildLabelsList(checkLabels(scanner.nextLine().toUpperCase()));
        Note note = noteDao.createNote(checkedText, checkedLabelsList);
        log.info("Заметка добавлена");
        return note;
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

    @Override
    public List<Note> getAllNotesList() {
        return noteDao.getAllNotesList();
    }

    @Override
    public void findNotesByLabels() throws Exception {
        logCallCommand("note-list");
        checkIfListNotesIsEmpty();
        log.info("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок");
        List<Note> matchingNotes = noteDao.findNotesByLabels(buildLabelsList(checkLabels(new Scanner(System.in).nextLine())));
        matchingNotes.forEach(note -> log.info(note.toString()));
    }

    private void checkIfListNotesIsEmpty() throws Exception {
        if (noteDao.getAllNotesList().isEmpty()) {
            log.info("Заметок не найдено");
            throw new Exception("Заметок не найдено");
        }
    }

    private Long checkId(String id) throws NumberFormatException {
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

    private List<Label> buildLabelsList(String checkedLabels) {
        return Stream.of(checkedLabels.replaceAll("\\s+", " ").strip().split(" ")).map(Label::new).toList();
    }

    @Override
    public void removeNoteById() throws Exception {
        logCallCommand("note-remove");
        checkIfListNotesIsEmpty();
        log.info("введите id удаляемой заметки");
        noteDao.removeNoteById(checkId(new Scanner(System.in).nextLine()));
        log.info("Заметка удалена");
    }

    @Override
    public void exportNotesToFile() throws Exception {
        logCallCommand("note-export");
        checkIfListNotesIsEmpty();
        String dirName = "notes";
        String fileName = "notes_" + new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss").format(new Date()) + ".txt";
        noteDao.exportNotesToFile(dirName, buildFilePath(dirName, fileName));
        log.info("Успешное завершение экспорта");
    }

    private Path buildFilePath(String dirName, String fileName) throws RuntimeException {
        return Path.of(dirName + "/" + fileName);
    }

    @Override
    public void exit() {
        logCallCommand("exit");
        System.exit(0);
    }
}
