import type {IQuizItem} from "../IQuizItem.ts";
import {SlideType} from "../../enums/TypeItem.ts";

export interface ISlideText2 extends IQuizItem {
    type: SlideType.SLIDE_TEXT_2;
    position: number;
    contentHeader: string;
    contentSubHeaderOne: string;
    contentTextOne: string;
    contentSubHeaderTwo: string;
    contentTextTwo: string;
}