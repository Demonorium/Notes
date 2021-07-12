package com.demonorium.webinterface.view;

import java.util.ArrayList;
import java.util.Arrays;

public class ShareNoteView {
    String name;
    ArrayList<String> content;

    public ShareNoteView(){

    }

    public ShareNoteView(String name, String content) {
        this.name = name;
        this.content = new ArrayList<>();
        String[] split = content.split("\\n");
        this.content.addAll(Arrays.asList(split));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
}
