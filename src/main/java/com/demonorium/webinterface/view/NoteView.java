package com.demonorium.webinterface.view;

public class NoteView {
    private String name;
    private String content;

    public NoteView(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public NoteView() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
