package com.pi.core_live.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Teacher {
    private final String login;
    private final String code;
    private final Control control;

    public Teacher(String login, String code) {
        this.login = login;
        this.code = code;
        this.control = new Control();
    }

    public String getLogin() { return login; }
    public String getCode() { return code; }
    public Control getControl() { return control; }

    public void nextPosition() { control.nextPosition(); }
    public void previousPosition() { control.previousPosition(); }

    public static class Control {
        private Integer currentPosition;

        public Control() { this.currentPosition = 0; }

        public Integer getCurrentPosition() { return currentPosition; }
        public void setCurrentPosition(Integer currentPosition) { this.currentPosition = currentPosition; }

        public void nextPosition() { this.currentPosition++; }
        public void previousPosition() { this.currentPosition--; }
    }
}
