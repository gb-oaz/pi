import type {IScope} from "./types/IScope.ts";
import type {IStatus} from "./types/IStatus.ts";
import type {IToken} from "./types/IToken.ts";
import {ErrorType} from "../../utils/errors/enums/ErrorType.ts";
import {GlobalError} from "../../utils/errors/GlobalError.ts";
import {StatusType} from "./enums/StatusType.ts";

const GLOBAL_KEY_TOKEN = 'token';
const GLOBAL_KEY_SCOPE = 'scope';

const GLOBAL_ERROR_UNEXPECT_DETAIL = 'Unexpected error during request auth service';
const GLOBAL_ERROR_UNEXPECT_ACTION = 'Awaiting for support team to fix this issue';

export class AuthApi {
    private readonly baseUrl: string;

    constructor() { this.baseUrl = import.meta.env.VITE_MS_AUTH_URL; }

    async initAnonymousToken(): Promise<void> {
        const COMMAND_POST_ANONYMOUS_TOKEN = 'COMMAND_POST_ANONYMOUS_TOKEN';
        const URL = `${this.baseUrl}/auth/v1/post/anonymous/token/${COMMAND_POST_ANONYMOUS_TOKEN}`;
        const ERROR_DETAIL = 'Failed POST anonymous token';
        const ERROR_ACTION = 'Check if send command type are correct';
        const TOKEN = this.getTokenLocalStorage();

        try {
            if (!(TOKEN && await this.checkStatusToken(TOKEN.token))) {
                let response = await
                    fetch(
                        URL, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            }
                        }
                    ).catch(error => {
                        throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                    });
                let data = await response.json() as IToken;
                localStorage.setItem(GLOBAL_KEY_TOKEN, JSON.stringify(data));
                await this.initScopeToken(data.token);
            }
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR,GLOBAL_ERROR_UNEXPECT_DETAIL,GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
    }

    async initScopeToken(token: string): Promise<void> {
        const QUERY_GET_SCOPE_TOKEN = 'QUERY_GET_SCOPE_TOKEN';
        const URL = `${this.baseUrl}/auth/v1/get/scope/token/${QUERY_GET_SCOPE_TOKEN}`;
        const ERROR_DETAIL = 'Failed GET scope token';
        const ERROR_ACTION = 'Check if send token and query type are correct';

        try {
            let response = await
                fetch(
                    URL, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + token
                        }
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            let data = await response.json() as IScope;
            localStorage.setItem(GLOBAL_KEY_SCOPE, JSON.stringify(data));
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
    }

    async signIn(login: string, code: string, password: string): Promise<void> {
        const TOKEN = this.getTokenLocalStorage();
        const COMMAND_POST_SIGN_IN_TOKEN = 'COMMAND_POST_SIGN_IN_TOKEN';
        const URL = `${this.baseUrl}/auth/v1/post/sign/in/token/${COMMAND_POST_SIGN_IN_TOKEN}`;
        const LOGIN = 'login';
        const CODE = 'code';
        const PASSWORD = 'password';
        const ERROR_DETAIL = 'Failed POST sign in token';
        const ERROR_ACTION = 'Check if send command type,anonymous token, login, code, password are correct';

        try {
            let formData = new FormData();
            formData.append(LOGIN, login);
            formData.append(CODE, code);
            formData.append(PASSWORD, password);

            let response = await
                fetch(
                    URL, {
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: formData
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            let data = await response.json() as IToken;
            if (data.token) {
                this.clearLocalStorage();
                localStorage.setItem(GLOBAL_KEY_TOKEN, JSON.stringify(data));
                await this.initScopeToken(data.token);
            }
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
    }

    async signOut(): Promise<void> {
        this.clearLocalStorage();
        await this.initAnonymousToken();
    }

    async checkStatusToken(token: string): Promise<Boolean> {
        const QUERY_GET_STATUS_TOKEN = 'QUERY_GET_STATUS_TOKEN';
        const URL = `${this.baseUrl}/auth/v1/get/status/token/${QUERY_GET_STATUS_TOKEN}`;
        const ERROR_DETAIL = 'Failed GET status token';
        const ERROR_ACTION = 'Check if send token and query type are correct';

        try {
            if (!token) return false;
            let response = await
                fetch(
                    URL, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + token
                        }
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            let data = await response.json() as IStatus;
            return data.status === StatusType.ACTIVE;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return false;
    }

    public getTokenLocalStorage(): IToken | null {
        let token = localStorage.getItem(GLOBAL_KEY_TOKEN);
        return token ? JSON.parse(token) as IToken : null;
    }

    public getScopeLocalStorage(): IScope | null {
        let scope = localStorage.getItem(GLOBAL_KEY_SCOPE);
        return scope ? JSON.parse(scope) as IScope : null;
    }

    protected clearLocalStorage(): void {
        localStorage.removeItem(GLOBAL_KEY_TOKEN);
        localStorage.removeItem(GLOBAL_KEY_SCOPE);
    }
}