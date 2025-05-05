import type {IQuizItem} from "../IQuizItem.ts";
import type {IOperationsQuiz} from "../IOperationsQuiz.ts";
import {QuizType, StatusItem} from "../../enums/TypeItem.ts";

export interface IQuizOpen extends IQuizItem, IOperationsQuiz<string> {
    type: QuizType.QUIZ_OPEN;
    position: number;
    contentQuestion: string;
    quantityCharacters: number;
    answers: string[];
    timerSeconds: number;
    reward: number;
    answersLive?: Map<string, string[]>;
    status?: StatusItem;
}