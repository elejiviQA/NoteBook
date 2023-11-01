package org.noBrains.model;

import lombok.extern.java.Log;

import java.util.List;

@Log
public class Note {
    private final Long id;
    private final String text;
    private final List<Label> labels;

    public Note(NoteBook noteBook, String text, List<Label> labels) {
        this.text = text;
        this.labels = labels;
        id = noteBook.getNewId();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String sp = String.format("%n");
        sb.append("{").append(id).append("}#{").append(text).append("}").append(sp);
        if (!labels.get(0).toString().isEmpty()) {
            labels.forEach(label -> sb.append("{").append(label).append("};"));
            sb.deleteCharAt(sb.length() - 1).append(sp).append(sp).append("===================").append(sp);
        } else {
            sb.append(sp).append("===================").append(sp);
        }
        return sb.toString();
    }

    public List<Label> getLabels() {
        return labels;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }
}
