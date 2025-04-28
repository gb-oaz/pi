package com.pi.core_live.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Evaluation {
    private final Map<String, Set<Answer>> evaluation;

    public Evaluation() { this.evaluation = new HashMap<>(); }

    public Map<String, Set<Answer>> getEvaluation() { return evaluation; }

    public void addEvaluation(String pupil, Integer positionItem, List<String> answerItem, Boolean hit) {
        if (evaluation.containsKey(pupil)) {
            evaluation.get(pupil).add(new Answer(positionItem, answerItem, hit));
        } else {
            var answers = new HashSet<Answer>();
            answers.add(new Answer(positionItem, answerItem, hit));
            evaluation.put(pupil, answers);
        }
    }

    public Integer countCorrectAnswers() {
        return (int) evaluation.values().stream()
                .flatMap(Set::stream)
                .filter(Answer::hit)
                .count();
    }

    public Integer countIncorrectAnswers() {
        return (int) evaluation.values().stream()
                .flatMap(Set::stream)
                .filter(answer -> !answer.hit())
                .count();
    }

    public Integer countUnansweredAnswers() {
        return (int) evaluation.values().stream()
                .flatMap(Set::stream)
                .filter(answer -> answer.answer().isEmpty())
                .count();
    }

    public record Answer(Integer position, List<String> answer, Boolean hit) { }
}
