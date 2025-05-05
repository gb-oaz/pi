import type {IQuizItem} from "../IQuizItem.ts";
import {SlideType} from "../../enums/TypeItem.ts";

export interface ISlideTextMedia2 extends IQuizItem {
    type: SlideType.SLIDE_TEXT_MEDIA_2;
    position: number;
    contentSubHeaderOne: string;
    contentTextOne: string;
    contentMediaOne: string;
    contentSubHeaderTwo: string;
    contentTextTwo: string;
    contentMediaTwo: string;
}