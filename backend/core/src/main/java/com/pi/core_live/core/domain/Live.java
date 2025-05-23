package com.pi.core_live.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IOperationsQuiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.domain.quiz.QuizOpen;
import com.pi.core_quiz.core.domain.quiz.QuizWordCloud;
import com.pi.core_quiz.core.enums.QuizType;
import com.pi.utils.services.Utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Live {
    private String key;
    private String startedOn;
    private String updateOn;
    private String completedOn;
    private StatusLive status;
    private Engagement engagement;
    private Evaluation evaluation;
    private Quiz quiz;
    private Teacher teacher;
    private Set<String> lobby;

    public Live() {}

    public Live(String login, String code, Quiz quiz) {
        this.key = createKey(login, code);
        this.startedOn = Utils.now();
        this.updateOn = Utils.now();
        this.status = StatusLive.PENDING;
        this.engagement = new Engagement();
        this.evaluation = new Evaluation();
        this.quiz = quiz;
        this.teacher = new Teacher(login, code);
        this.lobby = new HashSet<>();
    }

    // Teacher operations
    public void nextPosition() {
        teacher.nextPosition();
        status = StatusLive.PROGRESS;
        updateOn = Utils.now();
        checkCurrentPositionIsMayorQuizSize();
    }

    public void previousPosition() {
        teacher.previousPosition();
        updateOn = Utils.now();
    }
    public void removePupilFromLobby(String pupilLogin, String pupilCode) {
        lobby.remove(pupilLogin + "#" + pupilCode);
        updateOn = Utils.now();
    }

    // Pupil operations
    public void addPupilAnswerToQuiz(String login, String code, List<String> answerItem) {
        quiz.getQuizes().stream().filter(item -> item.getPosition().equals(teacher.getControl().getCurrentPosition())).forEach(item -> {
            Utils.ifEnumGet(item.getType(), QuizType.class);
            if (item instanceof IOperationsQuiz<?>) {
                ((IOperationsQuiz<?>) item).addAnswerOperationLive(login, code, answerItem);
                validateAnswer(login, code, answerItem, item);
                calculateEngagementAndUpdate();
            }
        });
        updateOn = Utils.now();
    }

    // Live operations
    public void addPupilToLobby(String pupilLogin, String pupilCode) {
        lobby.add(pupilLogin + "#" + pupilCode);
        updateOn = Utils.now();
    }
    protected void checkCurrentPositionIsMayorQuizSize() {
        if (teacher.getControl().getCurrentPosition() > quiz.getQuizes().size()) {
            completeLive();
        }
    }
    public void completeLive() {
        status = StatusLive.COMPLETED;
        updateOn = Utils.now();
        completedOn = Utils.now();
    }

    protected void validateAnswer(String login, String code, List<String> answerItem, IQuizItem item) {
        var answers = ((IOperationsQuiz<?>) item).getAnswers();
        boolean hit = isHit(answerItem, item, answers);
        evaluation.addEvaluation(login + "#" + code, item.getPosition(), answerItem, hit);
        updateOn = Utils.now();
    }

    protected static boolean isHit(List<String> answerItem, IQuizItem item, List<String> answers) {
        if (item instanceof QuizWordCloud || item instanceof QuizOpen) {
            return !answers.contains("NOT_ANSWERED");
        }
        return new HashSet<String>(answerItem).containsAll(answers);
    }

    protected void calculateEngagementAndUpdate() {
        var participantCount = lobby.size();
        var answersCorrect = evaluation.countCorrectAnswers();
        var answersIncorrect = evaluation.countIncorrectAnswers();
        var answersUnanswered = evaluation.countUnansweredAnswers();
        engagement.update(participantCount, answersCorrect, answersIncorrect, answersUnanswered);
        updateOn = Utils.now();
    }

    protected static String createKey(String login, String code) {
        return "Live-" + Utils.generateRandomToken() + "-" + login + "#" + code;
    }

    // Builder
    public static Live builder() { return new Live(); }
    public Live key(String key) { this.key = key; return this; }
    public Live startedOn(String startedOn) { this.startedOn = startedOn; return this; }
    public Live updateOn(String updateOn) { this.updateOn = updateOn; return this; }
    public Live completedOn(String completedOn) { this.completedOn = completedOn; return this; }
    public Live status(StatusLive status) { this.status = status; return this; }
    public Live engagement(Engagement engagement) { this.engagement = engagement; return this; }
    public Live evaluation(Evaluation evaluation) { this.evaluation = evaluation; return this; }
    public Live quiz(Quiz quiz) { this.quiz = quiz; return this; }
    public Live teacher(Teacher teacher) { this.teacher = teacher; return this; }
    public Live lobby(Set<String> lobby) { this.lobby = lobby; return this; }

    public Live build() { return this; }

    // Getters
    public String getKey() { return key; }
    public String getStartedOn() { return startedOn; }
    public String getUpdateOn() { return updateOn; }
    public String getCompletedOn() { return completedOn; }
    public StatusLive getStatus() { return status; }
    public Engagement getEngagement() { return engagement; }
    public Evaluation getEvaluation() { return evaluation; }
    public Quiz getQuiz() { return quiz; }
    public Teacher getTeacher() { return teacher; }
    public Set<String> getLobby() { return lobby; }
}