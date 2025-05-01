package com.pi.core_quiz.core.domain.slide;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.SlideType;

import java.beans.ConstructorProperties;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class SlideTextMedia2 implements IQuizItem {
    private static final String TYPE = SlideType.SLIDE_TEXT_MEDIA_2.name();
    private Integer position;
    private String contentSubHeaderOne;
    private String contentTextOne;
    private String contentMediaOne;
    private String contentSubHeaderTwo;
    private String contentTextTwo;
    private String contentMediaTwo;

    @ConstructorProperties({
            "position",
            "contentSubHeaderOne",
            "contentTextOne",
            "contentMediaOne",
            "contentSubHeaderTwo",
            "contentTextTwo",
            "contentMediaTwo"
    })
    @JsonCreator
    public SlideTextMedia2(
            @JsonProperty("position") Integer position,
            @JsonProperty("contentSubHeaderOne") String contentSubHeaderOne,
            @JsonProperty("contentTextOne") String contentTextOne,
            @JsonProperty("contentMediaOne") String contentMediaOne,
            @JsonProperty("contentSubHeaderTwo") String contentSubHeaderTwo,
            @JsonProperty("contentTextTwo") String contentTextTwo,
            @JsonProperty("contentMediaTwo") String contentMediaTwo
    ) {
        this.position = position;
        this.contentSubHeaderOne = contentSubHeaderOne;
        this.contentTextOne = contentTextOne;
        this.contentMediaOne = contentMediaOne;
        this.contentSubHeaderTwo = contentSubHeaderTwo;
        this.contentTextTwo = contentTextTwo;
        this.contentMediaTwo = contentMediaTwo;
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
    public String getContentSubHeaderOne() {
        return contentSubHeaderOne;
    }
    public String getContentTextOne() {
        return contentTextOne;
    }
    public String getContentMediaOne() {
        return contentMediaOne;
    }
    public String getContentSubHeaderTwo() {
        return contentSubHeaderTwo;
    }
    public String getContentTextTwo() {
        return contentTextTwo;
    }
    public String getContentMediaTwo() {
        return contentMediaTwo;
    }

    // Setters
    public void setContentSubHeaderOne(String contentSubHeaderOne) { this.contentSubHeaderOne = contentSubHeaderOne; }
    public void setContentTextOne(String contentTextOne) { this.contentTextOne = contentTextOne; }
    public void setContentMediaOne(String contentMediaOne) { this.contentMediaOne = contentMediaOne; }
    public void setContentSubHeaderTwo(String contentSubHeaderTwo) { this.contentSubHeaderTwo = contentSubHeaderTwo; }
    public void setContentTextTwo(String contentTextTwo) { this.contentTextTwo = contentTextTwo; }
    public void setContentMediaTwo(String contentMediaTwo) { this.contentMediaTwo = contentMediaTwo; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SlideTextMedia2) obj;
        return Objects.equals(this.position, that.position) &&
                Objects.equals(this.contentSubHeaderOne, that.contentSubHeaderOne) &&
                Objects.equals(this.contentTextOne, that.contentTextOne) &&
                Objects.equals(this.contentMediaOne, that.contentMediaOne) &&
                Objects.equals(this.contentSubHeaderTwo, that.contentSubHeaderTwo) &&
                Objects.equals(this.contentTextTwo, that.contentTextTwo) &&
                Objects.equals(this.contentMediaTwo, that.contentMediaTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, contentSubHeaderOne, contentTextOne, contentMediaOne, contentSubHeaderTwo, contentTextTwo, contentMediaTwo);
    }

    @Override
    public String toString() {
        return "SlideTextMedia2[" +
                "position=" + position + ", " +
                "contentSubHeaderOne=" + contentSubHeaderOne + ", " +
                "contentTextOne=" + contentTextOne + ", " +
                "contentMediaOne=" + contentMediaOne + ", " +
                "contentSubHeaderTwo=" + contentSubHeaderTwo + ", " +
                "contentTextTwo=" + contentTextTwo + ", " +
                "contentMediaTwo=" + contentMediaTwo + ']';
    }
}
