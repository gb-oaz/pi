package com.pi.core_quiz.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.utils.services.Utils;

import java.util.LinkedHashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Quiz {
    private String key;
    private String login;
    private String code;
    private String name;
    private Set<IQuizItem> quizes;
    private Set<String> categories;

    public Quiz() { }

    public Quiz(String login, String code, String name, Set<String> categories) {
        this.key = Utils.generateRandomToken();
        this.login = login;
        this.code = code;
        this.name = name;
        this.quizes = new LinkedHashSet<>();
        this.categories = categories;
    }

    public static Quiz builder() { return new Quiz(); }
    public Quiz key(String key) { this.key = key; return this; }
    public Quiz login(String login) { this.login = login; return this; }
    public Quiz code(String code) { this.code = code; return this; }
    public Quiz name(String name) { this.name = name; return this; }
    public Quiz quizes(Set<IQuizItem> quizes) { this.quizes = quizes; return this; }
    public Quiz categories(Set<String> categories) { this.categories = categories; return this; }

    public Quiz build() { return this; }

    public String getKey() { return key; }
    public String getLogin() { return login; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public Set<IQuizItem> getQuizes() { return quizes; }
    public Set<String> getCategories() { return categories; }

    public void addQuizItem(Integer position, IQuizItem quizItem) {
        quizItem.setPosition(position);
        quizes.add(quizItem);
    }

    public void updateQuizItem(Integer position, IQuizItem item) {
        quizes.removeIf(quizItem -> quizItem.getPosition().equals(position));
        item.setPosition(position);
        quizes.add(item);
    }

    public void deleteQuizItem(Integer position) {
        quizes.removeIf(quizItem -> quizItem.getPosition().equals(position));
    }

    public Boolean exist(Integer position) {
        return quizes.stream().anyMatch(quizItem -> quizItem.getPosition().equals(position));
    }
}
