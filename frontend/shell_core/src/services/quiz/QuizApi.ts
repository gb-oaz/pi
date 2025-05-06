import type {IKey} from "./types/IKey.ts";
import type {IPageable} from "../../utils/types/IPageable.ts";
import type {IQuizItem} from "./types/IQuizItem.ts";
import type {IQuiz} from "./types/IQuiz.ts";
import type {IScope} from "../auth/types/IScope.ts";
import type {IToken} from "../auth/types/IToken.ts";
import {AuthApi} from "../auth/AuthApi.ts";
import {ErrorType} from "../../utils/errors/enums/ErrorType.ts";
import {GlobalError} from "../../utils/errors/GlobalError.ts";
import {ScopeType} from "../user/enums/ScopeType.ts";

const GLOBAL_ERROR_UNEXPECT_DETAIL = 'Unexpected error during request auth service';
const GLOBAL_ERROR_UNEXPECT_ACTION = 'Awaiting for support team to fix this issue';

const GLOBAL_LOGIN = 'login';
const GLOBAL_CODE = 'code';
const GLOBAL_NAME = 'name';
const GLOBAL_CATEGORIES = 'categories';

export class QuizApi {
    private readonly baseUrl: string;
    private readonly authApi = new AuthApi();

    constructor() { this.baseUrl = import.meta.env.VITE_MS_QUIZ_URL; }

    async createQuiz(quizName: string, queryCategories: string[]): Promise<IKey> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_POST_NEW_QUIZ = 'COMMAND_POST_NEW_QUIZ';
        const URL = `${this.baseUrl}/quiz/v1/post/new/quiz/${COMMAND_POST_NEW_QUIZ}`;
        const ERROR_DETAIL = 'Failed POST create quiz';
        const ERROR_ACTION = 'Check if send command type COMMAND_POST_NEW_QUIZ, teacher token, login, code, quizName, queryCategories are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {key: ''};
            let response = await
                fetch(
                    this.quizBuildURLWithQueryParams(queryCategories, URL),
                    {
                    method: 'POST',
                    headers: {
                        'Authorization': 'Bearer ' + TOKEN?.token
                    },
                    body: this.createQuizFormData(SCOPE.login, SCOPE.code, quizName)
                }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IKey;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {key: ''};
    }

    async updateQuiz(quizKey: string, quizName: string, queryCategories: string[]): Promise<IKey> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PUT_QUIZ = 'COMMAND_PUT_QUIZ';
        const URL = `${this.baseUrl}/quiz/v1/put/quiz/${quizKey}/${COMMAND_PUT_QUIZ}`;
        const ERROR_DETAIL = 'Failed PUT update quiz';
        const ERROR_ACTION = 'Check if send command type COMMAND_PUT_QUIZ, teacher token, quizName, queryCategories are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {key: ''};
            let response = await
                fetch(
                    this.quizBuildURLWithQueryParams(queryCategories, URL),
                    {
                        method: 'PUT',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: this.updateQuizFormData(quizName)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IKey;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {key: ''};
    }

    async deleteQuiz(quizKey: string): Promise<IKey> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_DELETE_QUIZ = 'COMMAND_DELETE_QUIZ';
        const URL = `${this.baseUrl}/quiz/v1/delete/quiz/${quizKey}/${COMMAND_DELETE_QUIZ}`;
        const ERROR_DETAIL = 'Failed DELETE quiz';
        const ERROR_ACTION = 'Check if send command type COMMAND_DELETE_QUIZ, teacher token, quizKey are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {key: ''};
            let response = await
                fetch(
                    URL,
                    {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IKey;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {key: ''};
    }

    async createQuizItem(quizKey: string, position: number, item: IQuizItem): Promise<IKey> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_POST_QUIZ_ITEM = 'COMMAND_POST_QUIZ_ITEM';
        const URL = `${this.baseUrl}/quiz/v1/post/quiz/${quizKey}/item/${position}/${COMMAND_POST_QUIZ_ITEM}`;
        const ERROR_DETAIL = 'Failed POST create quiz item';
        const ERROR_ACTION = 'Check if send command type COMMAND_POST_QUIZ_ITEM, teacher token, quizKey, position, item are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {key: ''};
            let response = await
                fetch(
                    this.quizItemBuildURLWithQueryParams(item.type, URL),
                    {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: JSON.stringify(item)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IKey;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {key: ''};
    }

    async updateQuizItem(quizKey: string, position: number, item: IQuizItem): Promise<IKey> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_QUIZ_ITEM = 'COMMAND_PATCH_QUIZ_ITEM';
        const URL = `${this.baseUrl}/quiz/v1/patch/quiz/${quizKey}/item/${position}/${COMMAND_PATCH_QUIZ_ITEM}`;
        const ERROR_DETAIL = 'Failed PATCH update quiz item';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_QUIZ_ITEM, teacher token, quizKey, position, item are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {key: ''};
            let response = await
                fetch(
                    this.quizItemBuildURLWithQueryParams(item.type, URL),
                    {
                        method: 'PATCH',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: JSON.stringify(item)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IKey;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {key: ''};
    }

    async deleteQuizItem(quizKey: string, position: number): Promise<IKey> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_DELETE_QUIZ_ITEM = 'COMMAND_DELETE_QUIZ_ITEM';
        const URL = `${this.baseUrl}/quiz/v1/delete/quiz/${quizKey}/item/${position}/${COMMAND_DELETE_QUIZ_ITEM}`;
        const ERROR_DETAIL = 'Failed DELETE quiz item';
        const ERROR_ACTION = 'Check if send command type COMMAND_DELETE_QUIZ_ITEM, teacher token, quizKey, position are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {key: ''};
            let response = await
                fetch(
                    URL,
                    {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IKey;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {key: ''};
    }

    async getQuizes(login?: string, code?: string, name?: string, categories?: string[], page?: number, size?: number): Promise<IPageable<IQuiz>> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const QUERY_GET_QUIZES_PROJECTION = 'QUERY_GET_QUIZES_PROJECTION';
        const URL = `${this.baseUrl}/quiz/v1/get/quizes/projection/${QUERY_GET_QUIZES_PROJECTION}`;
        const ERROR_DETAIL = 'Failed GET quizes';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_QUIZES_PROJECTION, your token and projection are correct';

        try {
            let response = await
                fetch(
                    this.getQuizesBuildURLWithQueryParams(login, code, name, categories, page, size, URL),
                    {
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }
                ).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IPageable<IQuiz>;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION,error).logError();
        }
        return {} as IPageable<IQuiz>;
    }

    async getQuiz(quizKey: string): Promise<IQuiz> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const QUERY_GET_QUIZ = 'QUERY_GET_QUIZ';
        const URL = `${this.baseUrl}/quiz/v1/get/quiz/${quizKey}/${QUERY_GET_QUIZ}`;
        const ERROR_DETAIL = 'Failed GET quiz';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_QUIZ, teacher token, quizKey are correct';

        try {
            let response = await
                fetch(
                    URL,
                    {
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IQuiz;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as IQuiz;
    }

    async getQuizItem(quizKey: string, position: number): Promise<IQuizItem> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const QUERY_GET_QUIZ_ITEM = 'QUERY_GET_QUIZ_ITEM';
        const URL = `${this.baseUrl}/quiz/v1/get/quiz/${quizKey}/item/${position}/${QUERY_GET_QUIZ_ITEM}`;
        const ERROR_DETAIL = 'Failed GET quiz item';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_QUIZ_ITEM, teacher token, quizKey are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {} as IQuizItem;
            let response = await
                fetch(
                    URL,
                    {
                        method: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        }
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as IQuizItem;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as IQuizItem;
    }

    protected createQuizFormData(login: string, code: string, quizName: string) {
        let formData = new FormData();
        formData.append(GLOBAL_LOGIN, login);
        formData.append(GLOBAL_CODE, code);
        formData.append(GLOBAL_NAME, quizName);
        return formData;
    }

    protected updateQuizFormData(quizName: string) {
        let formData = new FormData();
        formData.append(GLOBAL_NAME, quizName);
        return formData;
    }

    protected quizBuildURLWithQueryParams(queryCategories: string[] | undefined, URL: string) {
        const params = new URLSearchParams();
        if (queryCategories && queryCategories.length > 0) {
            queryCategories.forEach(category => {
                params.append(GLOBAL_CATEGORIES, category);
            });
        }
        const queryString = params.toString();
        return queryString ? `${URL}?${queryString}` : URL;
    }

    protected quizItemBuildURLWithQueryParams(typeItem: string, URL: string) {
        const params: Record<string, string> = {};
        params.typeItem = typeItem;

        const queryString = new URLSearchParams(params).toString();
        return queryString ? `${URL}?${queryString}` : URL;
    }

    protected getQuizesBuildURLWithQueryParams(login: string | undefined, code: string | undefined, name: string | undefined, categories: string[] | undefined, page: number | undefined, size: number | undefined, URL: string) {
        const params: Record<string, string> = {};
        if (login) params.login = login;
        if (code) params.code = code;
        if (name) params.name = name;
        if (page) params.page = page.toString();
        if (size) params.size = size.toString();

        const partialParams = new URLSearchParams(params);

        if (categories && categories.length > 0) {
            categories.forEach(category => {
                partialParams.append(GLOBAL_CATEGORIES, category);
            });
        }
        const queryString = partialParams.toString();
        return queryString ? `${URL}?${queryString}` : URL;
    }
}