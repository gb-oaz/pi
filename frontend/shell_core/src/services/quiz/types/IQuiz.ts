import type {IQuizItem} from "./IQuizItem.ts";

export interface IQuiz {
    key: string;
    login: string;
    code: string;
    name: string;
    quizes: IQuizItem[];
    categories: string[];
}