import type {IQuizItem} from "../IQuizItem.ts";
import type {IOperationsQuiz} from "../IOperationsQuiz.ts";
import {QuizType, StatusItem} from "../../enums/TypeItem.ts";

export interface IQuizPoll extends IQuizItem, IOperationsQuiz<string> {
    type: QuizType.QUIZ_POLL;
    position: number;
    contentQuestion: string;
    answers: string[];
    timerSeconds: number;
    answersLive?: Map<string, string[]>;
    status?: StatusItem;
}