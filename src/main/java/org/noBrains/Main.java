package org.noBrains;

import org.noBrains.dao.NoteDaoImpl;
import org.noBrains.model.NoteBook;
import org.noBrains.service.NoteServiceImpl;

public class Main {
    public static void main(String[] args) {
        new NoteServiceImpl(new NoteDaoImpl(new NoteBook())).launch();
    }
}
