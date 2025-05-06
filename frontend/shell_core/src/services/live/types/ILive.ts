import type {IEngagement} from "./IEngagement.ts";
import type {IEvaluation} from "./IEvaluation.ts";
import type {IQuiz} from "../../quiz/types/IQuiz.ts";
import type {ITeacher} from "./ITeacher.ts";
import type {StatusLive} from "../enums/StatusLive.ts";

export interface ILive {
    key: string;
    startedOn: string;
    updateOn: string;
    completedOn: string;
    status: StatusLive;
    engagement: IEngagement;
    evaluation: IEvaluation;
    quiz: IQuiz;
    teacher: ITeacher;
    lobby: string[];
}