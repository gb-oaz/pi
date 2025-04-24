package com.pi.core_quiz.core.domain.itens;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pi.core_quiz.core.domain.quiz.QuizFillSpace;
import com.pi.core_quiz.core.domain.quiz.QuizMultipleChoice;
import com.pi.core_quiz.core.domain.quiz.QuizOpen;
import com.pi.core_quiz.core.domain.quiz.QuizPoll;
import com.pi.core_quiz.core.domain.quiz.QuizTrueFalse;
import com.pi.core_quiz.core.domain.quiz.QuizWordCloud;
import com.pi.core_quiz.core.domain.slide.SlideText1;
import com.pi.core_quiz.core.domain.slide.SlideText2;
import com.pi.core_quiz.core.domain.slide.SlideTextMedia1;
import com.pi.core_quiz.core.domain.slide.SlideTextMedia2;
import com.pi.core_quiz.core.domain.slide.SlideTitle1;
import com.pi.core_quiz.core.domain.slide.SlideTitle2;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = QuizMultipleChoice.class, name = "QUIZ_MULTIPLE_CHOICE"),
        @JsonSubTypes.Type(value = QuizFillSpace.class, name = "QUIZ_FILL_SPACE"),
        @JsonSubTypes.Type(value = QuizOpen.class, name = "QUIZ_OPEN"),
        @JsonSubTypes.Type(value = QuizPoll.class, name = "QUIZ_POLL"),
        @JsonSubTypes.Type(value = QuizTrueFalse.class, name = "QUIZ_TRUE_FALSE"),
        @JsonSubTypes.Type(value = QuizWordCloud.class, name = "QUIZ_WORD_CLOUD"),
        @JsonSubTypes.Type(value = SlideText1.class, name = "SLIDE_TEXT_1"),
        @JsonSubTypes.Type(value = SlideText2.class, name = "SLIDE_TEXT_2"),
        @JsonSubTypes.Type(value = SlideTextMedia1.class, name = "SLIDE_TEXT_MEDIA_1"),
        @JsonSubTypes.Type(value = SlideTextMedia2.class, name = "SLIDE_TEXT_MEDIA_2"),
        @JsonSubTypes.Type(value = SlideTitle1.class, name = "SLIDE_TITLE_1"),
        @JsonSubTypes.Type(value = SlideTitle2.class, name = "SLIDE_TITLE_2")
})
public interface IQuizItem extends IPositionItem {
    String getType();
}
