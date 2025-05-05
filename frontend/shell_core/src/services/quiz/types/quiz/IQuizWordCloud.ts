import type {IQuizItem} from "../IQuizItem.ts";
import type {IOperationsQuiz} from "../IOperationsQuiz.ts";
import {QuizType, StatusItem} from "../../enums/TypeItem.ts";

export interface IQuizWordCloud extends IQuizItem, IOperationsQuiz<string> {
    type: QuizType.QUIZ_WORD_CLOUD;
    position: number;
    contentQuestion: string;
    quantityCharacters: number;
    answers: string[];
    timerSeconds: number;
    reward: number;
    answersLive?: Map<string, string[]>;
    status?: StatusItem;
}