package com.pi.core_quiz.core.domain.quiz;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IOperationsQuiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.QuizType;
import com.pi.core_quiz.core.enums.StatusItem;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class QuizOpen implements IQuizItem, IOperationsQuiz<String> {
    private static final String TYPE = QuizType.QUIZ_OPEN.name();
    private Integer position;
    private String contentQuestion;
    private Integer quantityCharacters;
    private List<String> answers;
    private Integer timerSeconds;
    private Integer reward;
    private Map<String, List<String>> answersLive;
    private StatusItem status;

    @ConstructorProperties({
            "position",
            "contentQuestion",
            "quantityCharacters",
            "answers",
            "timerSeconds",
            "reward",
            "answersLive",
            "status"
    })
    @JsonCreator
    public QuizOpen(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentQuestion") String contentQuestion,
            @JsonProperty("quantityCharacters") Integer quantityCharacters,
            @JsonProperty("answers") List<String> answers,
            @JsonProperty("timerSeconds") Integer timerSeconds,
            @JsonProperty("reward") Integer reward
    ) {
        this.position = position;
        this.contentQuestion = contentQuestion;
        this.quantityCharacters = quantityCharacters;
        this.answers = answers;
        this.timerSeconds = timerSeconds;
        this.reward = reward;
        this.answersLive = new HashMap<>();
        this.status = StatusItem.PENDING;
    }

    @Override @JsonProperty("type") public String getType() {
        return TYPE;
    }
    @Override public Integer getPosition() {
        return position;
    }
    @Override public void setPosition(Integer position) {
        this.position = position;
    }
    @Override public Map<String, List<String>> getAnswersLive() {
        return answersLive;
    }
    @Override public List<String> getAnswers() { return answers; }

    // Getters
    public String getContentQuestion() {
        return contentQuestion;
    }
    public Integer getQuantityCharacters() {
        return quantityCharacters;
    }
    public Integer getTimerSeconds() {
        return timerSeconds;
    }
    public Integer getReward() {
        return reward;
    }
    public StatusItem getStatus() {
        return status;
    }

    // Setters
    public void setContentQuestion(String contentQuestion) { this.contentQuestion = contentQuestion; }
    public void setQuantityCharacters(Integer quantityCharacters) { this.quantityCharacters = quantityCharacters; }
    public void setAnswers(List<String> answers) { this.answers = answers; }
    public void setTimerSeconds(Integer timerSeconds) { this.timerSeconds = timerSeconds; }
    public void setReward(Integer reward) { this.reward = reward; }
    public void setAnswersLive(Map<String, List<String>> answersLive) { this.answersLive = answersLive; }
    public void setStatus(StatusItem status) { this.status = status; }

    @Override
    public void addAnswerOperationLive(String login, String code, List<String> value) { addAnswerLive(login,code,value); }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (QuizOpen) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentQuestion, that.contentQuestion) &&
                Objects.equals(this.quantityCharacters, that.quantityCharacters) &&
                Objects.equals(this.answers, that.answers) &&
                Objects.equals(this.timerSeconds, that.timerSeconds) &&
                Objects.equals(this.reward, that.reward) &&
                Objects.equals(this.answersLive, that.answersLive) &&
                Objects.equals(this.status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentQuestion, quantityCharacters, answers, timerSeconds, reward, answersLive, status);
    }

    @Override
    public String toString() {
        return "QuizOpen[" +
                "position=" + position + ", " +
                "contentQuestion=" + contentQuestion + ", " +
                "quantityCharacters=" + quantityCharacters + ", " +
                "answers=" + answers + ", " +
                "timerSeconds=" + timerSeconds + ", " +
                "reward=" + reward + ", " +
                "answersLive=" + answersLive + ", " +
                "status=" + status + ']';
    }

}
