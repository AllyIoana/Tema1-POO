package com.example.project;

public class Question {
    private static int currentID = 1;
    int id;
    String text;
    boolean single; // 0 - single; 1 - multiple
    int noAnswers;
    Answer[] answers;

    Question() {
    }

    Question(String text, boolean single, int noAnswers, Answer[] answers) {
        this.id = currentID;
        currentID++;
        this.text = text;
        this.single = single;
        this.noAnswers = noAnswers;
        this.answers = answers;
        int i;
        for (i = 0; i < noAnswers; i++) {
            answers[i].newID();
        }
    }

    Question(String line) {
        String[] data = line.split(",");
        this.id = Integer.parseInt(data[0]);
        this.text = data[1];
        if (data[2].equals("true"))
            this.single = true;
        else single = false;
        this.noAnswers = Integer.parseInt(data[3]);
        this.answers = new Answer[5];
        Answer tempAnswer;
        int i;
        for (i = 0; i < this.noAnswers; i++) {
            boolean correct;
            if (data[4 + 3 * i + 2].equals("true"))
                correct = true;
            else correct = false;
            tempAnswer = new Answer(Integer.parseInt(data[4 + 3 * i]), data[4 + 3 * i + 1], correct);
            answers[i] = tempAnswer;
        }
    }

    public String toString() {
        String temp;
        int i;
        temp = this.id + "," + this.text + "," + this.single + "," + this.noAnswers + ",";
        for (i = 0; i < this.noAnswers; i++) {
            temp += this.answers[i].id + "," + this.answers[i].text + "," + this.answers[i].correct;
            if (i != noAnswers - 1) temp += ",";
        }
        return temp;
    }

    public float negativePoints() {
        if (this.single)
            return 1;
        int no = 0;
        for (int i = 0; i < this.noAnswers; i++)
            if (!this.answers[i].correct)
                no++;
        return 1.0f / no;
    }

    public float pozitivePoints() {
        if (this.single)
            return 1;
        int yes = 0;
        for (int i = 0; i < this.noAnswers; i++)
            if (this.answers[i].correct)
                yes++;
        return 1.0f / yes;
    }

    public void resetID() {
        currentID = 1;
    }


    public void setId(int id) {
        this.id = id;
    }
}
