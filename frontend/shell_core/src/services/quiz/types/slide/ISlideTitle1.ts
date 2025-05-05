import type {IQuizItem} from "../IQuizItem.ts";
import {SlideType} from "../../enums/TypeItem.ts";

export interface ISlideTitle1 extends IQuizItem {
    type: SlideType.SLIDE_TITLE_1;
    position: number;
    contentTitle: string;
}