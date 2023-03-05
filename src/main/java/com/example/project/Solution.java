package com.example.project;

public class Solution {
    String user;
    Quiz quiz;

    public Solution(String user, Quiz quiz) {
        this.user = user;
        this.quiz = quiz;
    }

    Solution(String line) {
        this.user = line.split(",")[0];
        this.quiz = new Quiz();
        this.quiz.setId(Integer.parseInt(line.split(",")[1]));
        this.quiz.name = line.split(",")[2];
        this.quiz.points = Float.parseFloat(line.split(",")[3]);
    }

    public String toString() {
        return this.user + "," + quiz.id + "," + quiz.name + "," + quiz.points;
    }
}
