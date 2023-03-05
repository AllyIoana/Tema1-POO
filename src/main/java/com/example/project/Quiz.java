package com.example.project;

public class Quiz {
    private static int currentID = 1;
    int id;
    String name;
    String user;
    boolean completed;
    int noQuestions;
    Question[] questions;
    float points;

    Quiz() {
    }

    public Quiz(String name, String user, int noQuestions, Question[] questions) {
        this.id = currentID;
        currentID++;
        this.name = name;
        this.user = user;
        this.completed = false;
        this.noQuestions = noQuestions;
        this.questions = questions;
    }

    Quiz(String line) {
        // pentru o linie din Quizzes.csv
        String[] data = line.split(",");
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.user = data[2];
        switch (data[3]) {
            case "True":
                completed = true;
                break;
            case "False":
                completed = false;
                break;
        }
        this.noQuestions = Integer.parseInt(data[4]);
        this.questions = new Question[5 + 10];
        for (int i = 0; i < this.noQuestions; i++) {
            this.questions[i] = new Question();
            this.questions[i].setId(Integer.parseInt(data[4 + i]));
        }
    }

    public String toString() {
        String temp = this.id + "," + this.name + "," + this.user + ",";
        if (this.completed)
            temp += "True";
        else temp += "False";
        temp += "," + this.noQuestions;
        int i;
        for (i = 0; i < this.noQuestions; i++)
            temp += "," + this.questions[i].id;
        return temp;
    }

    public String details() {
        String temp = "[";
        boolean notFirstQuestion = false;
        int i, j;
        for (i = 0; i < this.noQuestions; i++) {
            if (notFirstQuestion) temp += ", ";
            else notFirstQuestion = true;
            temp += "{\"question-name\":\"" + this.questions[i].text + "\", \"question_index\":\"" + this.questions[i].id
                    + "\", \"question_type\":" + (this.questions[i].single ? "\"single\"" : "\"multiple\"") + ", \"answers\":\"[";
            boolean notFirstAnswer = false;
            for (j = 0; j < this.questions[i].noAnswers; j++) {
                if (notFirstAnswer) temp += ", ";
                else notFirstAnswer = true;
                temp += "{\"answer_name\":\"" + this.questions[i].answers[j].text + "\", \"answer_id\":\"" + this.questions[i].answers[j].id + "\"}";
            }
            temp += "]\"}";
        }
        temp += "]";
        return temp;
    }

    public float pozitivePoints(Question question) {
        return (100.0f / this.noQuestions) * question.pozitivePoints();
    }

    public float negativePoints(Question question) {
        return (100.0f / this.noQuestions) * question.negativePoints();
    }

    public void resetID() {
        currentID = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setNoQuestions(int noQuestions) {
        this.noQuestions = noQuestions;
    }

    public void setQuestions(Question[] questions) {
        this.questions = questions;
    }

    public void setPoints() {
        this.points = 0;
    }
}
