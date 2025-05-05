import type {IQuizItem} from "../IQuizItem.ts";
import type {IOperationsQuiz} from "../IOperationsQuiz.ts";
import {QuizType, StatusItem} from "../../enums/TypeItem.ts";

export interface IQuizFillSpace extends IQuizItem, IOperationsQuiz<string> {
    type: QuizType.QUIZ_FILL_SPACE;
    position: number;
    contentQuestion: string;
    answers: string[];
    timerSeconds: number;
    reward: number;
    answersLive?: Map<string, string[]>;
    status?: StatusItem;
}