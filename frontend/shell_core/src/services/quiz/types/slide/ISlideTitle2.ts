import type {IQuizItem} from "../IQuizItem.ts";
import {SlideType} from "../../enums/TypeItem.ts";

export interface ISlideTitle2 extends IQuizItem {
    type: SlideType.SLIDE_TITLE_2;
    position: number;
    contentTitle: string;
    contentDescription: string;
}