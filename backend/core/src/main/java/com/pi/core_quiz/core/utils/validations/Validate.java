package com.pi.core_quiz.core.utils.validations;

import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.domain.quiz.*;
import com.pi.core_quiz.core.domain.slide.*;
import com.pi.core_quiz.core.enums.QuizType;
import com.pi.core_quiz.core.enums.SlideType;
import com.pi.utils.constants.Regex;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.validations.ValidationCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Set;

public class Validate extends ValidationCommon {
    private static final Logger LOG = LoggerFactory.getLogger(Validate.class);
    private static final String LOG_VALIDATE_QUIZ_TYPE = "Init validate quizType format: {}";
    private static final String LOG_VALIDATE_SLIDE_TYPE = "Init validate slideType format: {}";
    private static final String LOG_VALIDATE_QUIZ_QUIZ_ITEM = "Init validate quiz quizItem format: {}";
    private static final String LOG_VALIDATE_SLIDE_QUIZ_ITEM = "Init validate slide quizItem format: {}";

    public static void name(String name) {
        LOG.info("Init validate name format: {}", name);
        if (ObjectUtils.isEmpty(name) || !name.matches(Regex.NAME)) {
            LOG.warn("Field name is empty or not matches.");
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C103PI))
                    .build();
        }
    }

    public static void key(String key) {
        LOG.info("Init validate key format: {}", key);
        if (ObjectUtils.isEmpty(key)) {
            LOG.warn("Field key is empty.");
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C101PI))
                    .build();
        }
    }

    public static void position(Integer position) {
        LOG.info("Init validate position format: {}", position);
        if (ObjectUtils.isEmpty(position)) {
            LOG.warn("Field position is empty.");
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C102PI))
                    .build();
        }
    }

    public static void categories(Set<String> categories) {
        LOG.info("Init validate categories format: {}", categories);
        if (ObjectUtils.isEmpty(categories)) {
            LOG.warn("Field categories is empty.");
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C104PI))
                    .build();
        }
    }

    public static void quizType(QuizType quizType, IQuizItem quizItem) {
        LOG.info(LOG_VALIDATE_QUIZ_TYPE, quizType.name());
        LOG.info(LOG_VALIDATE_QUIZ_QUIZ_ITEM, quizItem.getType());

        switch (quizType) {
            case QUIZ_MULTIPLE_CHOICE -> {
                QuizMultipleChoice quiz = (QuizMultipleChoice) quizItem;
                if (ObjectUtils.isEmpty(quiz.getContentQuestion())) throw errorField("contentQuestion");
                if (ObjectUtils.isEmpty(quiz.getAlternatives())) throw errorField("alternatives");
                if (ObjectUtils.isEmpty(quiz.getAnswers())) throw errorField("answers");
                if (ObjectUtils.isEmpty(quiz.getTimerSeconds())) throw errorField("timerSeconds");
                if (ObjectUtils.isEmpty(quiz.getReward())) throw errorField("reward");
                break;
            }
            case QUIZ_FILL_SPACE -> {
                QuizFillSpace quiz = (QuizFillSpace) quizItem;
                if (ObjectUtils.isEmpty(quiz.getContentQuestion())) throw errorField("contentQuestion");
                if (ObjectUtils.isEmpty(quiz.getAnswers())) throw errorField("answers");
                if (ObjectUtils.isEmpty(quiz.getTimerSeconds())) throw errorField("timerSeconds");
                if (ObjectUtils.isEmpty(quiz.getReward())) throw errorField("reward");
                break;
            }
            case QUIZ_TRUE_FALSE -> {
                QuizTrueFalse quiz = (QuizTrueFalse) quizItem;
                if (ObjectUtils.isEmpty(quiz.getContentQuestion())) throw errorField("contentQuestion");
                if (ObjectUtils.isEmpty(quiz.getAnswers())) throw errorField("answers");
                if (ObjectUtils.isEmpty(quiz.getTimerSeconds())) throw errorField("timerSeconds");
                if (ObjectUtils.isEmpty(quiz.getReward())) throw errorField("reward");
                break;
            }
            case QUIZ_OPEN -> {
                QuizOpen quiz = (QuizOpen) quizItem;
                if (ObjectUtils.isEmpty(quiz.getContentQuestion())) throw errorField("contentQuestion");
                if (ObjectUtils.isEmpty(quiz.getQuantityCharacters())) throw errorField("quantityCharacters");
                if (ObjectUtils.isEmpty(quiz.getAnswers())) throw errorField("answers");
                if (ObjectUtils.isEmpty(quiz.getTimerSeconds())) throw errorField("timerSeconds");
                if (ObjectUtils.isEmpty(quiz.getReward())) throw errorField("reward");
                break;
            }
            case QUIZ_POLL -> {
                QuizPoll quiz = (QuizPoll) quizItem;
                if (ObjectUtils.isEmpty(quiz.getContentQuestion())) throw errorField("contentQuestion");
                if (ObjectUtils.isEmpty(quiz.getAnswers())) throw errorField("answers");
                if (ObjectUtils.isEmpty(quiz.getTimerSeconds())) throw errorField("timerSeconds");
            }
            case QUIZ_WORD_CLOUD -> {
                QuizWordCloud quiz = (QuizWordCloud) quizItem;
                if (ObjectUtils.isEmpty(quiz.getContentQuestion())) throw errorField("contentQuestion");
                if (ObjectUtils.isEmpty(quiz.getQuantityCharacters())) throw errorField("quantityCharacters");
                if (ObjectUtils.isEmpty(quiz.getAnswers())) throw errorField("answers");
                if (ObjectUtils.isEmpty(quiz.getTimerSeconds())) throw errorField("timerSeconds");
                break;
            }
            default -> {
                LOG.warn("{} - Invalid quizType.", SystemCodeEnum.C090PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C090PI))
                        .build();
            }
        }
    }

    public static void slideType(SlideType slideType, IQuizItem quizItem) {
        LOG.info(LOG_VALIDATE_SLIDE_TYPE, slideType.name());
        LOG.info(LOG_VALIDATE_SLIDE_QUIZ_ITEM, quizItem.getType());

        switch (slideType) {
            case SLIDE_TITLE_1 -> {
                SlideTitle1 slide = (SlideTitle1) quizItem;
                if (ObjectUtils.isEmpty(slide.getContentTitle())) throw errorField("contentTitle");
                break;
            }
            case SLIDE_TITLE_2 -> {
                SlideTitle2 slide = (SlideTitle2) quizItem;
                if (ObjectUtils.isEmpty(slide.getContentTitle())) throw errorField("contentTitle");
                if (ObjectUtils.isEmpty(slide.getContentDescription())) throw errorField("contentDescription");
                break;
            }
            case SLIDE_TEXT_1 -> {
                SlideText1 slide = (SlideText1) quizItem;
                if (ObjectUtils.isEmpty(slide.getContentHeader())) throw errorField("contentHeader");
                if (ObjectUtils.isEmpty(slide.getContentSubHeaderOne())) throw errorField("contentSubHeaderOne");
                if (ObjectUtils.isEmpty(slide.getContentTextOne())) throw errorField("contentTextOne");
                break;
            }
            case SLIDE_TEXT_2 -> {
                SlideText2 slide = (SlideText2) quizItem;
                if (ObjectUtils.isEmpty(slide.getContentHeader())) throw errorField("contentHeader");
                if (ObjectUtils.isEmpty(slide.getContentSubHeaderOne())) throw errorField("contentSubHeaderOne");
                if (ObjectUtils.isEmpty(slide.getContentTextOne())) throw errorField("contentTextOne");
                if (ObjectUtils.isEmpty(slide.getContentSubHeaderTwo())) throw errorField("contentSubHeaderTwo");
                if (ObjectUtils.isEmpty(slide.getContentTextTwo())) throw errorField("contentTextTwo");
                break;
            }
            case SLIDE_TEXT_MEDIA_1 -> {
                SlideTextMedia1 slide = (SlideTextMedia1) quizItem;
                if (ObjectUtils.isEmpty(slide.getContentHeader())) throw errorField("contentHeader");
                if (ObjectUtils.isEmpty(slide.getContentTextOne())) throw errorField("contentTextOne");
                if (ObjectUtils.isEmpty(slide.getContentMediaOne())) throw errorField("contentMediaOne");
                break;
            }
            case SLIDE_TEXT_MEDIA_2 -> {
                SlideTextMedia2 slide = (SlideTextMedia2) quizItem;
                if (ObjectUtils.isEmpty(slide.getContentSubHeaderOne())) throw errorField("contentSubHeaderOne");
                if (ObjectUtils.isEmpty(slide.getContentTextOne())) throw errorField("contentTextOne");
                if (ObjectUtils.isEmpty(slide.getContentMediaOne())) throw errorField("contentMediaOne");
                if (ObjectUtils.isEmpty(slide.getContentSubHeaderTwo())) throw errorField("contentSubHeaderTwo");
                if (ObjectUtils.isEmpty(slide.getContentTextTwo())) throw errorField("contentTextTwo");
                if (ObjectUtils.isEmpty(slide.getContentMediaTwo())) throw errorField("contentMediaTwo");
                break;
            }
            default -> {
                LOG.warn("{} - Invalid slideType.", SystemCodeEnum.C092PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C092PI))
                        .build();
            }
        }
    }

    protected static GlobalException errorField(String field) {
        LOG.warn("{} - Invalid {} field.", SystemCodeEnum.C091PI.name(), field);
        return GlobalException.builder()
                .status(400)
                .alert(new CustomAlert(SystemCodeEnum.C091PI))
                .details("Field " + field + " is empty.")
                .build();
    }
}
