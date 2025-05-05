import type {IQuizItem} from "./IQuizItem.ts";

export interface IQuiz {
    key: string;
    login: string;
    code: string;
    name: string;
    quizes: Set<IQuizItem>;
    categories: Set<string>;
}