import type {IQuizItem} from "../IQuizItem.ts";
import {SlideType} from "../../enums/TypeItem.ts";

export interface ISlideText1 extends IQuizItem {
    type: SlideType.SLIDE_TEXT_1;
    position: number;
    contentHeader: string;
    contentSubHeaderOne: string;
    contentTextOne: string;
}