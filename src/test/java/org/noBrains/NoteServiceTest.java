package org.noBrains;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.noBrains.dao.NoteDaoImpl;
import org.noBrains.model.Note;
import org.noBrains.model.NoteBook;
import org.noBrains.service.NoteService;
import org.noBrains.service.NoteServiceImpl;

public class NoteServiceTest {
    private NoteService noteService;
    private NoteBook noteBook;

    private void readFromConsole(String text) {
        System.setIn(new ByteArrayInputStream(text.getBytes()));
    }

    @BeforeEach
    void prepare() {
        noteBook = new NoteBook();
        noteService = new NoteServiceImpl(new NoteDaoImpl(noteBook));
    }

    @DisplayName("Создание заметки с заполнением всех полей корректными значениями")
    @Test
    public void testCreateNote_WithCorrectTextAndLabel() throws Exception {
        readFromConsole("text\nlabel");
        Note note = noteService.createNewNote();
        assertTrue(note.getId() == 1 && note.getText().equals("text") && note.getLabels().size() == 1 && note.getLabels().get(0).toString().equals("LABEL"));
    }

    @DisplayName("Создание заметки без меток")
    @Test
    public void testCreateNote_WithoutLabel() throws Exception {
        readFromConsole("text\n\n");
        Note note = noteService.createNewNote();
        assertTrue(note.getId() == 1 && note.getText().equals("text") && note.getLabels().get(0).toString().isEmpty());
    }

    @DisplayName("Создание заметки с текстом менее 3 символов")
    @Test
    public void testCreateNote_WithTextLessThanMinCharacters() {
        readFromConsole("te\nlabel");
        assertThrows(Exception.class, noteService::createNewNote);
    }

    @DisplayName("Создание заметки с некорректным значением метки")
    @Test
    public void testCreateNote_WithAnIncorrectLabelValue() {
        readFromConsole("text\n23");
        assertThrows(Exception.class, noteService::createNewNote);
    }

    @DisplayName("Уникальность id при создании заметок")
    @Test
    public void testGenId_ConfirmUnique() throws Exception {
        readFromConsole("text\nlabel");
        Note note = noteService.createNewNote();
        readFromConsole("text2\nanotherLabel");
        Note note2 = noteService.createNewNote();
        assertNotEquals(note.getId(), note2.getId());
    }

    @DisplayName("Удаление заметки с несуществующим id")
    @Test
    public void testRemoveNote_WithNonExistentId() throws Exception {
        readFromConsole("text\nlabel");
        noteService.createNewNote();
        readFromConsole("2");
        assertThrows(Exception.class, noteService::removeNoteById);
        assertEquals(1, noteBook.getNotes().size());
    }

    @DisplayName("Удаление заметки с некорректным типом данных id")
    @Test
    public void testRemoveNote_WithIncorrectDataType() throws Exception {
        readFromConsole("text\nlabel");
        noteService.createNewNote();
        readFromConsole("q");
        assertThrows(Exception.class, noteService::removeNoteById);
        assertEquals(1, noteBook.getNotes().size());
    }

    @DisplayName("Потеря id после удаления заметки")
    @Test
    public void testGenId_ConfirmDoesNotRepeat() throws Exception {
        readFromConsole("text\nlabel");
        Note note = noteService.createNewNote();
        readFromConsole("1");
        noteService.removeNoteById();
        readFromConsole("text2\nanotherLabel");
        Note note2 = noteService.createNewNote();
        assertTrue(note.getId() == 1 && note2.getId() == 2);
    }
}
