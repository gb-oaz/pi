package com.pi.core_quiz.core.domain.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.QuizType;
import com.pi.core_quiz.core.enums.StatusQuiz;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class QuizFillSpace implements IQuizItem {
    private static final String TYPE = QuizType.QUIZ_FILL_SPACE.name();
    private Integer position;
    private String contentQuestion;
    private List<String> answers;
    private Integer timerSeconds;
    private Integer reward;
    private Map<String, List<Object>> answersLive;
    private StatusQuiz status;

    @ConstructorProperties({
            "position",
            "contentQuestion",
            "answers",
            "timerSeconds",
            "reward",
            "answersLive",
            "status"
    })
    @JsonCreator
    public QuizFillSpace(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentQuestion") String contentQuestion,
            @JsonProperty("answers") List<String> answers,
            @JsonProperty("timerSeconds") Integer timerSeconds,
            @JsonProperty("reward") Integer reward
    ) {
        this.position = position;
        this.contentQuestion = contentQuestion;
        this.answers = answers;
        this.timerSeconds = timerSeconds;
        this.reward = reward;
        this.answersLive = new HashMap<>();
        this.status = StatusQuiz.PENDING;
    }

    @Override public String getType() {
        return TYPE;
    }
    @Override public Integer getPosition() {
        return position;
    }
    @Override public void setPosition(Integer position) {
        this.position = position;
    }

    // Getters
    public String getContentQuestion() {
        return contentQuestion;
    }
    public List<String> getAnswers() {
        return answers;
    }
    public Integer getTimerSeconds() {
        return timerSeconds;
    }
    public Integer getReward() {
        return reward;
    }
    public Map<String, List<Object>> getAnswersLive() {
        return answersLive;
    }
    public StatusQuiz getStatus() {
        return status;
    }

    // Setters
    public void setContentQuestion(String contentQuestion) { this.contentQuestion = contentQuestion; }
    public void setAnswers(List<String> answers) { this.answers = answers; }
    public void setTimerSeconds(Integer timerSeconds) { this.timerSeconds = timerSeconds; }
    public void setReward(Integer reward) { this.reward = reward; }
    public void setAnswersLive(Map<String, List<Object>> answersLive) { this.answersLive = answersLive; }
    public void setStatus(StatusQuiz status) { this.status = status; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (QuizFillSpace) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentQuestion, that.contentQuestion) &&
                Objects.equals(this.answers, that.answers) &&
                Objects.equals(this.timerSeconds, that.timerSeconds) &&
                Objects.equals(this.reward, that.reward) &&
                Objects.equals(this.answersLive, that.answersLive) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentQuestion, answers, timerSeconds, reward, answersLive, status);
    }

    @Override
    public String toString() {
        return "QuizFillSpace[" +
                "position=" + position + ", " +
                "contentQuestion=" + contentQuestion + ", " +
                "answers=" + answers + ", " +
                "timerSeconds=" + timerSeconds + ", " +
                "reward=" + reward + ", " +
                "answersLive=" + answersLive + ", " +
                "status=" + status + ']';
    }
}
