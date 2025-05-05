import type { StatusType } from "../enums/StatusType.ts";

export interface IToken {
    token: string;
    createAt: string;
    expiryAt: string;
    status: StatusType;
}