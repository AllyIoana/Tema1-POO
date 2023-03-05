package com.example.project;

public class Answer {
    private static int currentID = 1;
    int id;
    String text;
    boolean correct;

    Answer() {
        text = "";
        correct = false;
    }

    Answer(int id, String text, boolean correct) {
        this.id = id;
        this.text = text;
        this.correct = correct;
    }

    public static void resetID() {
        currentID = 1;
    }

    public void newID() {
        this.id = currentID;
        currentID++;
    }
}
