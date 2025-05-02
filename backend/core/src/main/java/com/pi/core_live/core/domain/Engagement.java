package com.pi.core_live.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Engagement {
    private Integer participantCount;
    private Integer answersCorrect;
    private Integer answersIncorrect;
    private Integer answersUnanswered;
    private Integer correctPercentual;
    private Integer incorrectPercentual;
    private Integer unansweredPercentual;

    public Engagement() {
        this.participantCount = 0;
        this.answersCorrect = 0;
        this.answersIncorrect = 0;
        this.answersUnanswered = 0;
        this.correctPercentual = 0;
        this.incorrectPercentual = 0;
        this.unansweredPercentual = 0;
    }

    public void update(Integer participantCount, Integer answersCorrect, Integer answersIncorrect, Integer answersUnanswered) {
        this.participantCount = participantCount;
        this.answersCorrect = answersCorrect;
        this.answersIncorrect = answersIncorrect;
        this.answersUnanswered = answersUnanswered;
        this.correctPercentual = calculateCorrectPercentual();
        this.incorrectPercentual = calculateIncorrectPercentual();
        this.unansweredPercentual = calculateUnansweredPercentual();
    }

    protected Integer getTotalAnswers() {
        return answersCorrect + answersIncorrect + answersUnanswered;
    }

    public Integer calculateCorrectPercentual() {
        int total = getTotalAnswers();
        if (total == 0) return 0;
        return (int) Math.round((answersCorrect * 100.0) / total);
    }

    public Integer calculateIncorrectPercentual() {
        int total = getTotalAnswers();
        if (total == 0) return 0;
        return (int) Math.round((answersIncorrect * 100.0) / total);
    }

    public Integer calculateUnansweredPercentual() {
        int total = getTotalAnswers();
        if (total == 0) return 0;
        return (int) Math.round((answersUnanswered * 100.0) / total);
    }

    // Getters
    public Integer getParticipantCount() { return participantCount; }
    public Integer getAnswersCorrect() { return answersCorrect; }
    public Integer getAnswersIncorrect() { return answersIncorrect; }
    public Integer getAnswersUnanswered() { return answersUnanswered; }
    public Integer getCorrectPercentual() { return correctPercentual; }
    public Integer getIncorrectPercentual() { return incorrectPercentual; }
    public Integer getUnansweredPercentual() { return unansweredPercentual; }

    // Setters
    public void setParticipantCount(Integer participantCount) { this.participantCount = participantCount; }
    public void setAnswersCorrect(Integer answersCorrect) { this.answersCorrect = answersCorrect; }
    public void setAnswersIncorrect(Integer answersIncorrect) { this.answersIncorrect = answersIncorrect; }
    public void setAnswersUnanswered(Integer answersUnanswered) { this.answersUnanswered = answersUnanswered; }
    public void setCorrectPercentual(Integer correctPercentual) { this.correctPercentual = correctPercentual; }
    public void setIncorrectPercentual(Integer incorrectPercentual) { this.incorrectPercentual = incorrectPercentual; }
    public void setUnansweredPercentual(Integer unansweredPercentual) { this.unansweredPercentual = unansweredPercentual; }
}
