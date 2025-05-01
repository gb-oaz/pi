package com.pi.core_quiz.core.domain.slide;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.SlideType;

import java.beans.ConstructorProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SlideText1 implements IQuizItem {
    private static final String TYPE = SlideType.SLIDE_TEXT_1.name();
    private Integer position;
    private String contentHeader;
    private String contentSubHeaderOne;
    private String contentTextOne;

    @ConstructorProperties({
            "position",
            "contentHeader",
            "contentSubHeaderOne",
            "contentTextOne"
    })
    @JsonCreator
    public SlideText1(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentHeader") String contentHeader,
            @JsonProperty("contentSubHeaderOne") String contentSubHeaderOne,
            @JsonProperty("contentTextOne") String contentTextOne
    ) {
        this.position = position;
        this.contentHeader = contentHeader;
        this.contentSubHeaderOne = contentSubHeaderOne;
        this.contentTextOne = contentTextOne;
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

    // Getters
    public String getContentHeader() {
        return contentHeader;
    }
    public String getContentSubHeaderOne() {
        return contentSubHeaderOne;
    }
    public String getContentTextOne() {
        return contentTextOne;
    }

    // Setters
    public void setContentHeader(String contentHeader) { this.contentHeader = contentHeader; }
    public void setContentSubHeaderOne(String contentSubHeaderOne) { this.contentSubHeaderOne = contentSubHeaderOne; }
    public void setContentTextOne(String contentTextOne) { this.contentTextOne = contentTextOne; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SlideText1) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentHeader, that.contentHeader) &&
                Objects.equals(this.contentSubHeaderOne, that.contentSubHeaderOne) &&
                Objects.equals(this.contentTextOne, that.contentTextOne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentHeader, contentSubHeaderOne, contentTextOne);
    }

    @Override
    public String toString() {
        return "SlideText1[" +
                "position=" + position + ", " +
                "contentHeader=" + contentHeader + ", " +
                "contentSubHeaderOne=" + contentSubHeaderOne + ", " +
                "contentTextOne=" + contentTextOne + ']';
    }
}
