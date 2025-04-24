package com.pi.core_quiz.core.domain.slide;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.SlideType;

import java.beans.ConstructorProperties;
import java.util.Objects;

public final class SlideTitle1 implements IQuizItem {
    private static final String TYPE = SlideType.SLIDE_TITLE_1.name();
    private Integer position;
    private String contentTitle;

    @ConstructorProperties({
            "position",
            "contentTitle"
    })
    @JsonCreator
    public SlideTitle1(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentTitle") String contentTitle
    ) {
        this.position = position;
        this.contentTitle = contentTitle;
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
    public String getContentTitle() {
        return contentTitle;
    }

    // Setters
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SlideTitle1) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentTitle, that.contentTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentTitle);
    }

    @Override
    public String toString() {
        return "SlideTitle1[" +
                "position=" + position + ", " +
                "contentTitle=" + contentTitle + ']';
    }
}
