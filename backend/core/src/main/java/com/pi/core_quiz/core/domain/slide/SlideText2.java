package com.pi.core_quiz.core.domain.slide;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.SlideType;

import java.beans.ConstructorProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SlideText2 implements IQuizItem {
    private static final String TYPE = SlideType.SLIDE_TEXT_2.name();
    private Integer position;
    private String contentHeader;
    private String contentSubHeaderOne;
    private String contentTextOne;
    private String contentSubHeaderTwo;
    private String contentTextTwo;

    @ConstructorProperties({
            "position",
            "contentHeader",
            "contentSubHeaderOne",
            "contentTextOne",
            "contentSubHeaderTwo",
            "contentTextTwo"
    })
    @JsonCreator
    public SlideText2(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentHeader") String contentHeader,
            @JsonProperty("contentSubHeaderOne") String contentSubHeaderOne,
            @JsonProperty("contentTextOne") String contentTextOne,
            @JsonProperty("contentSubHeaderTwo") String contentSubHeaderTwo,
            @JsonProperty("contentTextTwo") String contentTextTwo
    ) {
        this.position = position;
        this.contentHeader = contentHeader;
        this.contentSubHeaderOne = contentSubHeaderOne;
        this.contentTextOne = contentTextOne;
        this.contentSubHeaderTwo = contentSubHeaderTwo;
        this.contentTextTwo = contentTextTwo;
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
    public String getContentSubHeaderTwo() {
        return contentSubHeaderTwo;
    }
    public String getContentTextTwo() {
        return contentTextTwo;
    }

    // Setters
    public void setContentHeader(String contentHeader) { this.contentHeader = contentHeader; }
    public void setContentSubHeaderOne(String contentSubHeaderOne) { this.contentSubHeaderOne = contentSubHeaderOne; }
    public void setContentTextOne(String contentTextOne) { this.contentTextOne = contentTextOne; }
    public void setContentSubHeaderTwo(String contentSubHeaderTwo) { this.contentSubHeaderTwo = contentSubHeaderTwo; }
    public void setContentTextTwo(String contentTextTwo) { this.contentTextTwo = contentTextTwo; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SlideText2) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentHeader, that.contentHeader) &&
                Objects.equals(this.contentSubHeaderOne, that.contentSubHeaderOne) &&
                Objects.equals(this.contentTextOne, that.contentTextOne) &&
                Objects.equals(this.contentSubHeaderTwo, that.contentSubHeaderTwo) &&
                Objects.equals(this.contentTextTwo, that.contentTextTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentHeader, contentSubHeaderOne, contentTextOne, contentSubHeaderTwo, contentTextTwo);
    }

    @Override
    public String toString() {
        return "SlideText2[" +
                "position=" + position + ", " +
                "contentHeader=" + contentHeader + ", " +
                "contentSubHeaderOne=" + contentSubHeaderOne + ", " +
                "contentTextOne=" + contentTextOne + ", " +
                "contentSubHeaderTwo=" + contentSubHeaderTwo + ", " +
                "contentTextTwo=" + contentTextTwo + ']';
    }
}
