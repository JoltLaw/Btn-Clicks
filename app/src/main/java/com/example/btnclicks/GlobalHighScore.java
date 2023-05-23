package com.example.btnclicks;

public class GlobalHighScore {

    int score;
    String email;

    public GlobalHighScore() {

    }
    public GlobalHighScore(int score, String email) {
        this.score = score;
        this.email = email;
    }

    public int getScore() {
        return  this.score;
    }
    public String getEmail() {
        return  this.email;
    }

    public void setScore(int newScore) {
        this.score = newScore;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }
}
