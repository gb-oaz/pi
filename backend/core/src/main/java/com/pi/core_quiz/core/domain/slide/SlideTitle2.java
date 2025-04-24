package com.pi.core_quiz.core.domain.slide;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.SlideType;

import java.beans.ConstructorProperties;
import java.util.Objects;

public final class SlideTitle2 implements IQuizItem {
    private static final String TYPE = SlideType.SLIDE_TITLE_2.name();
    private Integer position;
    private String contentTitle;
    private String contentDescription;

    @ConstructorProperties({
            "position",
            "contentTitle",
            "contentDescription"
    })
    @JsonCreator
    public SlideTitle2(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentTitle") String contentTitle,
            @JsonProperty("contentDescription") String contentDescription
    ) {
        this.position = position;
        this.contentTitle = contentTitle;
        this.contentDescription = contentDescription;
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
    public String getContentDescription() {
        return contentDescription;
    }

    // Setters
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }
    public void setContentDescription(String contentDescription) { this.contentDescription = contentDescription; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SlideTitle2) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentTitle, that.contentTitle) &&
                Objects.equals(this.contentDescription, that.contentDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentTitle, contentDescription);
    }

    @Override
    public String toString() {
        return "SlideTitle2[" +
                "position=" + position + ", " +
                "contentTitle=" + contentTitle + ", " +
                "contentDescription=" + contentDescription + ']';
    }
}
