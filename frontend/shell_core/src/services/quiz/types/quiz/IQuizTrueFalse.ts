import type {IQuizItem} from "../IQuizItem.ts";
import type {IOperationsQuiz} from "../IOperationsQuiz.ts";
import {QuizType, StatusItem} from "../../enums/TypeItem.ts";

export interface IQuizTrueFalse extends IQuizItem, IOperationsQuiz<string> {
    type: QuizType.QUIZ_TRUE_FALSE;
    position: number;
    contentQuestion: string;
    answers: string[];
    timerSeconds: number;
    reward: number;
    answersLive?: Map<string, string[]>;
    status?: StatusItem;
}