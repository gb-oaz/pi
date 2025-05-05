import {ErrorType} from "./enums/ErrorType.ts";

export class GlobalError extends Error {
    constructor(
        public code: ErrorType,
        public details?: string,
        public action?: string,
        public originalError?: unknown
    ) {
        super(details);
    }

    logError(): void {
        console.groupCollapsed(
            "%cCUSTOM GLOBAL ERROR",
            "color: white; background-color: black; padding: 1px 6px; border-radius: 4px;"
        );

        console.info(
            "%cCode:%c " + this.code,
            "color: black;",
            "color: white;"
        );

        console.info(
            "%cDetails:%c " + this.details,
            "color: black;",
            "color: white;"
        );

        console.info(
            "%cAction:%c " + this.action,
            "color: black;",
            "color: white;"
        );

        console.error(
            "%cOriginal error:%c " + this.originalError,
            "color: white; background-color: black; padding: 1px 6px; border-radius: 4px;",
            "color: white;"
        );

        console.groupEnd();
    }
}