package com.pi.core_quiz.core.domain.slide;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.SlideType;

import java.beans.ConstructorProperties;
import java.util.Objects;

public final class SlideTextMedia1 implements IQuizItem {
    private static final String TYPE = SlideType.SLIDE_TEXT_MEDIA_1.name();
    private Integer position;
    private String contentHeader;
    private String contentTextOne;
    private String contentMediaOne;

    @ConstructorProperties({
            "position",
            "contentHeader",
            "contentTextOne",
            "contentMediaOne"
    })
    @JsonCreator
    public SlideTextMedia1(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentHeader") String contentHeader,
            @JsonProperty("contentTextOne") String contentTextOne,
            @JsonProperty("contentMediaOne") String contentMediaOne
    ) {
        this.position = position;
        this.contentHeader = contentHeader;
        this.contentTextOne = contentTextOne;
        this.contentMediaOne = contentMediaOne;
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
    public String getContentHeader() {
        return contentHeader;
    }
    public String getContentTextOne() {
        return contentTextOne;
    }
    public String getContentMediaOne() {
        return contentMediaOne;
    }

    // Setters
    public void setContentHeader(String contentHeader) { this.contentHeader = contentHeader; }
    public void setContentTextOne(String contentTextOne) { this.contentTextOne = contentTextOne; }
    public void setContentMediaOne(String contentMediaOne) { this.contentMediaOne = contentMediaOne; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SlideTextMedia1) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentHeader, that.contentHeader) &&
                Objects.equals(this.contentTextOne, that.contentTextOne) &&
                Objects.equals(this.contentMediaOne, that.contentMediaOne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentHeader, contentTextOne, contentMediaOne);
    }

    @Override
    public String toString() {
        return "SlideTextMedia1[" +
                "position=" + position + ", " +
                "contentHeader=" + contentHeader + ", " +
                "contentTextOne=" + contentTextOne + ", " +
                "contentMediaOne=" + contentMediaOne + ']';
    }
}
