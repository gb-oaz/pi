import type {IQuizItem} from "../IQuizItem.ts";
import type {IOperationsQuiz} from "../IOperationsQuiz.ts";
import {QuizType, StatusItem} from "../../enums/TypeItem.ts";

export interface IQuizMultipleChoice extends IQuizItem, IOperationsQuiz<string> {
    type: QuizType.QUIZ_MULTIPLE_CHOICE;
    position: number;
    contentQuestion: string;
    alternatives: string[];
    answers: string[];
    timerSeconds: number;
    reward: number;
    answersLive?: Map<string, string[]>;
    status?: StatusItem;
}