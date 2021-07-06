package com.demonorium.webinterface.view;

public class SearchView {
    private String input = "";
    private Integer target = 1;

    public SearchView() {
    }

    public SearchView(String input, Integer target) {
        this.input = input;
        this.target = target;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public Integer getTarget() {
        return target;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }
}
