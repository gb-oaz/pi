import type {IPageable} from "../../utils/types/IPageable.ts";
import type {IScope} from "../auth/types/IScope.ts";
import type {IToken} from "../auth/types/IToken.ts";
import type {IUser} from "./types/IUser.ts";
import {AuthApi} from "../auth/AuthApi.ts";
import {ErrorType} from "../../utils/errors/enums/ErrorType.ts";
import {GlobalError} from "../../utils/errors/GlobalError.ts";
import {ScopeType} from "./enums/ScopeType.ts";

const GLOBAL_ERROR_UNEXPECT_DETAIL = 'Unexpected error during request auth service';
const GLOBAL_ERROR_UNEXPECT_ACTION = 'Awaiting for support team to fix this issue';

const GLOBAL_NAME = 'name';
const GLOBAL_EMAIL = 'email';
const GLOBAL_LOGIN = 'login';
const GLOBAL_CODE = 'code';
const GLOBAL_PASSWORD = 'password';
const GLOBAL_PASSWORD_OLD = 'oldPassword';

export class UserApi {
    private readonly baseUrl: string;
    private readonly authApi = new AuthApi();

    constructor() { this.baseUrl = import.meta.env.VITE_MS_USER_URL; }

    async createStudent(name: string, email: string, login: string, code: string, password: string): Promise<Boolean> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const COMMAND_POST_CREATE_USER_STUDENT = 'COMMAND_POST_CREATE_USER_STUDENT';
        const URL = `${this.baseUrl}/user/v1/post/create/user/student/${COMMAND_POST_CREATE_USER_STUDENT}`;
        const ERROR_DETAIL = 'Failed POST create student';
        const ERROR_ACTION = 'Check if send command type COMMAND_POST_CREATE_USER_STUDENT, anonymous token, name, email, login, code, password are correct';

        try {
            let response = await this.createUser(name, email, login, code, password, URL, TOKEN, ERROR_DETAIL, ERROR_ACTION);
            let data = await response.json() as IUser;
            return data.scopes.includes(ScopeType.STUDENT);
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return false;
    }

    async createTeacher(name: string, email: string, login: string, code: string, password: string): Promise<Boolean> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const COMMAND_POST_CREATE_USER_TEACHER = 'COMMAND_POST_CREATE_USER_TEACHER';
        const URL = `${this.baseUrl}/user/v1/post/create/user/teacher/${COMMAND_POST_CREATE_USER_TEACHER}`;
        const ERROR_DETAIL = 'Failed POST create teacher';
        const ERROR_ACTION = 'Check if send command type COMMAND_POST_CREATE_USER_TEACHER, anonymous token, name, email, login, code, password are correct';

        try {
            let response = await this.createUser(name, email, login, code, password, URL, TOKEN, ERROR_DETAIL, ERROR_ACTION);
            let data = await response.json() as IUser;
            return data.scopes.includes(ScopeType.TEACHER);
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return false;
    }

    async updateUser(name?: string, email?: string, newPassword?: string, oldPassword?: string): Promise<Boolean> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PUT_UPDATE_USER = 'COMMAND_PUT_UPDATE_USER';
        const URL = `${this.baseUrl}/user/v1/put/update/user/${COMMAND_PUT_UPDATE_USER}`;
        const ERROR_DETAIL = 'Failed PUT update user';
        const ERROR_ACTION = 'Check if send command type COMMAND_PUT_UPDATE_USER, your token, name, email, newPassword, oldPassword are correct';

        try {
            if (SCOPE.scope.includes(ScopeType.ANONYMOUS)) return false;
            let response = await
                fetch(
                    URL, {
                        method: 'PUT',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: this.updateUserFormData(name, email, newPassword, oldPassword)
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            let data = await response.json() as IUser;
            return data.scopes.includes(ScopeType.TEACHER) || data.scopes.includes(ScopeType.STUDENT);
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return false;
    }

    async getUser(email?: string, login?: string, code?: string): Promise<IUser> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const QUERY_GET_USER_BY_PROJECTION = 'QUERY_GET_USER_BY_PROJECTION';
        const URL = `${this.baseUrl}/user/v1/get/user/by/projection/${QUERY_GET_USER_BY_PROJECTION}`;
        const ERROR_DETAIL = 'Failed GET user';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_USER_BY_PROJECTION, your token and projection are correct';

        try {
            let response = await
                fetch(
                    this.getUserBuildURLWithQueryParams(email, login, code, URL),
                    {
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IUser;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return {} as IUser;
    }

    async getUsers(name?: string, email?: string, login?: string, code?: string, page?: number, size?: number): Promise<IPageable<IUser>> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const QUERY_GET_USERS_BY_PROJECTION = 'QUERY_GET_USERS_BY_PROJECTION';
        const URL = `${this.baseUrl}/user/v1/get/users/by/projection/${QUERY_GET_USERS_BY_PROJECTION}`;
        const ERROR_DETAIL = 'Failed GET user';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_USERS_BY_PROJECTION, your token and projection are correct';

        try {
            let response = await
                fetch(
                    this.getUsersBuildURLWithQueryParams(name, email, login, code, page, size, URL),
                    {
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IPageable<IUser>;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return {} as IPageable<IUser>;
    }

    protected async createUser(name: string, email: string, login: string, code: string, password: string, URL: string, TOKEN: IToken, ERROR_DETAIL: string, ERROR_ACTION: string) {
        return await
            fetch(
                URL, {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + TOKEN?.token
                    },
                    body: this.createUserFormData(name, email, login, code, password)
                }
            ).catch(error => {
                throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
            });
    }

    protected createUserFormData(name: string, email: string, login: string, code: string, password: string) {
        let formData = new FormData();
        formData.append(GLOBAL_NAME, name);
        formData.append(GLOBAL_EMAIL, email);
        formData.append(GLOBAL_LOGIN, login);
        formData.append(GLOBAL_CODE, code);
        formData.append(GLOBAL_PASSWORD, password);
        return formData;
    }

    protected updateUserFormData(name: string | undefined, email: string | undefined, newPassword: string | undefined, oldPassword: string | undefined) {
        let formData = new FormData();
        if (name) formData.append(GLOBAL_NAME, name);
        if (email) formData.append(GLOBAL_EMAIL, email);
        if (newPassword) formData.append(GLOBAL_PASSWORD, newPassword);
        if (oldPassword) formData.append(GLOBAL_PASSWORD_OLD, oldPassword);
        return formData;
    }

    protected getUserBuildURLWithQueryParams(email: string | undefined, login: string | undefined, code: string | undefined, URL: string) {
        const params: Record<string, string> = {};
        if (email) params.email = email;
        if (login) params.login = login;
        if (code) params.code = code;

        const queryString = new URLSearchParams(params).toString();
        return queryString ? `${URL}?${queryString}` : URL;
    }

    protected getUsersBuildURLWithQueryParams(name: string | undefined, email: string | undefined, login: string | undefined, code: string | undefined, page: number | undefined, size: number | undefined, URL: string) {
        const params: Record<string, string> = {};
        if (name) params.name = name;
        if (email) params.email = email;
        if (login) params.login = login;
        if (code) params.code = code;
        if (page) params.page = page.toString();
        if (size) params.size = size.toString();

        const queryString = new URLSearchParams(params).toString();
        return queryString ? `${URL}?${queryString}` : URL;
    }
}