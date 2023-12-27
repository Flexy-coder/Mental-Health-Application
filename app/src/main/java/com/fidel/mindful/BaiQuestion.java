package com.fidel.mindful;

public class BaiQuestion {
    private final String question;
    private final String[] responses;

    public BaiQuestion(String question, String[] responses) {
        this.question = question;
        this.responses = responses;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getResponses() {
        return responses;
    }
}

