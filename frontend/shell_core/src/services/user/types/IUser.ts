import type {ScopeType} from "../enums/ScopeType.ts";

export interface IUser {
    name: string,
    email: string,
    login: string,
    code: string,
    password: string,
    createAt: string,
    updateAt: string,
    scopes: ScopeType[]
}