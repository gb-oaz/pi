import type {IQuizItem} from "../IQuizItem.ts";
import {SlideType} from "../../enums/TypeItem.ts";

export interface ISlideTextMedia1 extends IQuizItem {
    type: SlideType.SLIDE_TEXT_MEDIA_1;
    position: number;
    contentHeader: string;
    contentTextOne: string;
    contentMediaOne: string;
}