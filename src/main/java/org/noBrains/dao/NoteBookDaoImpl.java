package org.noBrains.dao;

import java.util.ArrayList;
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
    public void help() {
        callCommandLog("help");
        System.out.println("""
                help - выводит на экран список доступных команд с их описанием
                note-new  - создать новую заметку
                note-list - выводит все заметки на экран
                note-remove - удаляет заметку
                note-export - сохраняет все заметки в текстовый файл и выводит имя сохраненного файла
                exit - выход из приложения
                """);
    }

    @Override
    public void createNote() {
        callCommandLog("note-new");
        try {
            System.out.println("Введите заметку");
            String text = scanner.nextLine();
            checkText(text);
            System.out.println("Добавить метки? Метки состоят из одного слова и могу содержать только буквы. Для добавления нескольких меток разделяйте слова пробелом.");
            String labels = scanner.nextLine();
            checkLabels(labels);
            buildNote(text, labels);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void getNotes() {
        callCommandLog("note-list");
        List<Note> notes = getNotesList();
        try {
            sayIfEmpty(notes);
            System.out.println("Введите метки, чтобы отобразить определенные заметки или оставьте пустым для отображения всех заметок");
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                System.out.println(toString(notes));
            } else {
                System.out.println(toString(matches(buildLabels(checkLabels(line)))));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

    private void buildNote(String text, String checkedLabels) {
        Note note = new Note(text, buildLabels(checkedLabels));
        setNote(note);
        System.out.println(note);
        System.out.println("Заметка добавлена");
    }

    private List<Label> buildLabels(String checkedLabels) {
        return Stream.of(checkedLabels.replaceAll("\\s+", " ").strip().split(" ")).map(Label::new).toList();
    }

    private String toString(List<Note> notes) {
        StringBuilder sb = new StringBuilder();
        try {
            sayIfEmpty(notes);
            for (Note note : notes) {
                List<Label> labels = note.getLabels();
                sb.append("{").append(note.getId()).append("}#{").append(note.getText()).append("}\n");
                if (!labels.get(0).toString().isEmpty()) {
                    labels.forEach(label -> sb.append("{").append(label).append("};"));
                    sb.deleteCharAt(sb.length() - 1).append("\n\n===================\n\n");
                } else {
                    sb.append("\n===================\n\n");
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
        List<Note> notes = getNotesList();
        List<Label> noteLabels;
        for (Note note : notes) {
            int count = 0;
            for (Label checkedLabel : checkedListLabels) {
                if (count == 1) {
                    break;
                }
                noteLabels = note.getLabels();
                for (Label noteLabel : noteLabels) {
                    if (checkedLabel.toString().equals(noteLabel.toString())) {
                        result.add(note);
                        count = 1;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private void callCommandLog(String command) {
        log.fine("вызвана команда " + command);
    }

    private void sayIfEmpty(List<Note> notes) throws Exception {
        if (notes.isEmpty()) {
            log.info("Заметок не найдено");
            throw new Exception("Заметок не найдено");
        }
    }
}
