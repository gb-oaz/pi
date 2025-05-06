import {AuthApi} from "../auth/AuthApi.ts";
import type {ILive} from "./types/ILive.ts";
import type {IToken} from "../auth/types/IToken.ts";
import type {IScope} from "../auth/types/IScope.ts";
import {ScopeType} from "../user/enums/ScopeType.ts";
import {GlobalError} from "../../utils/errors/GlobalError.ts";
import {ErrorType} from "../../utils/errors/enums/ErrorType.ts";

const GLOBAL_ERROR_UNEXPECT_DETAIL = 'Unexpected error during request auth service';
const GLOBAL_ERROR_UNEXPECT_ACTION = 'Awaiting for support team to fix this issue';

const GLOBAL_LOGIN = 'login';
const GLOBAL_CODE = 'code';
const GLOBAL_PUPIL_LOGIN = 'pupilLogin';
const GLOBAL_PUPIL_CODE = 'pupilCode';
const GLOBAL_KEY_QUIZ = 'keyQuiz';
const GLOBAL_KEY_LIVE = 'keyLive';
const GLOBAL_ANSWER_ITEM = 'answerItem';

export class LiveApi {
    private readonly baseUrl: string;
    private readonly authApi = new AuthApi();

    constructor() { this.baseUrl = import.meta.env.VITE_MS_LIVE_URL; }

    async createLive(quizKey: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_POST_NEW_LIVE = 'COMMAND_POST_NEW_LIVE';
        const URL = `${this.baseUrl}/live/v1/post/new/live/${COMMAND_POST_NEW_LIVE}`;
        const ERROR_DETAIL = 'Failed POST create live';
        const ERROR_ACTION = 'Check if send command type COMMAND_POST_NEW_LIVE, teacher token, quizKey are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {} as ILive;
            let response = await
                fetch(
                    URL,
                    {
                        method: 'POST',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: this.createLiveFormData(SCOPE.login, SCOPE.code, quizKey)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async addPupilToLobby(keyLive: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_ADD_PUPIL_TO_LOBBY = 'COMMAND_PATCH_ADD_PUPIL_TO_LOBBY';
        const URL = `${this.baseUrl}/live/v1/patch/add/pupil/to/lobby/${COMMAND_PATCH_ADD_PUPIL_TO_LOBBY}`;
        const ERROR_DETAIL = 'Failed PATCH add pupil to live';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_ADD_PUPIL_TO_LOBBY, teacher token, keyLive are correct';

        try {
            let response = await
                fetch(
                    URL,
                    {
                        method: 'PATCH',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: this.pupilOperationsToLiveFormData(SCOPE.login, SCOPE.code, keyLive)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async removePupilToLobby(pupilLogin: string, pupilCode: string, keyLive: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY = 'COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY';
        const URL = `${this.baseUrl}/live/v1/patch/remove/pupil/from/lobby/${COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY}`;
        const ERROR_DETAIL = 'Failed PATCH remove pupil to live';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY, teacher token, pupilLogin, pupilCode, keyLive are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {} as ILive;
            let response = await
                fetch(
                    URL,
                    {
                        method: 'PATCH',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: this.removePupilToLiveFormData(SCOPE.login, SCOPE.code, pupilLogin, pupilCode, keyLive)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async nextPosition(keyLive: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_NEXT_POSITION = 'COMMAND_PATCH_NEXT_POSITION';
        const URL = `${this.baseUrl}/live/v1/patch/next/position/${COMMAND_PATCH_NEXT_POSITION}`;
        const ERROR_DETAIL = 'Failed PATCH next position to live';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_NEXT_POSITION, teacher token, keyLive are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {} as ILive;
            let response = await this.teacherPatchOperations(URL, TOKEN, SCOPE, keyLive, ERROR_DETAIL, ERROR_ACTION);
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async addAnswerPupil(keyLive: string, answerItem: string[]): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ = 'COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ';
        const URL = `${this.baseUrl}/live/v1/patch/add/pupil/answer/to/quiz/${COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ}`;
        const ERROR_DETAIL = 'Failed PATCH add pupil answer to live';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ, teacher token, keyLive, answerItem are correct';

        try {
            let response = await
                fetch(
                    this.liveBuildURLWithQueryParams(answerItem, URL),
                    {
                        method: 'PATCH',
                        headers: {
                            'Authorization': 'Bearer ' + TOKEN?.token
                        },
                        body: this.pupilOperationsToLiveFormData(SCOPE.login, SCOPE.code, keyLive)
                    }).catch(error => {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                });
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async previousPosition(keyLive: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_PREVIOUS_POSITION = 'COMMAND_PATCH_PREVIOUS_POSITION';
        const URL = `${this.baseUrl}/live/v1/patch/previous/position/${COMMAND_PATCH_PREVIOUS_POSITION}`;
        const ERROR_DETAIL = 'Failed PATCH previous position to live';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_PREVIOUS_POSITION, teacher token, keyLive are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {} as ILive;
            let response = await this.teacherPatchOperations(URL, TOKEN, SCOPE, keyLive, ERROR_DETAIL, ERROR_ACTION);
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async endLive(keyLive: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const SCOPE = this.authApi.getScopeLocalStorage() as IScope;
        const COMMAND_PATCH_END_LIVE = 'COMMAND_PATCH_END_LIVE';
        const URL = `${this.baseUrl}/live/v1/patch/end/live/${COMMAND_PATCH_END_LIVE}`;
        const ERROR_DETAIL = 'Failed PATCH end live';
        const ERROR_ACTION = 'Check if send command type COMMAND_PATCH_END_LIVE, teacher token, keyLive are correct';

        try {
            if (!SCOPE.scope.includes(ScopeType.TEACHER)) return {} as ILive;
            let response = await this.teacherPatchOperations(URL, TOKEN, SCOPE, keyLive, ERROR_DETAIL, ERROR_ACTION);
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async getLive(keyLive: string): Promise<ILive> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const QUERY_GET_LIVE = 'QUERY_GET_LIVE';
        const URL = `${this.baseUrl}/live/v1/get/live/${QUERY_GET_LIVE}/${keyLive}`;
        const ERROR_DETAIL = 'Failed GET live';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_LIVE, teacher token, keyLive are correct';

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
            return await response.json() as ILive;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
        }
        return {} as ILive;
    }

    async getLiveStream(
        keyLive: string,
        onMessage: (data: ILive) => void,
        onError?: (error: Event) => void
    ): Promise<EventSource> {
        const TOKEN = this.authApi.getTokenLocalStorage() as IToken;
        const QUERY_GET_LIVE_STREAM = 'QUERY_GET_LIVE_STREAM';
        const URL = `${this.baseUrl}/live/v1/get/live/stream/${QUERY_GET_LIVE_STREAM}/${keyLive}`;
        const ERROR_DETAIL = 'Failed GET live stream';
        const ERROR_ACTION = 'Check if send command type QUERY_GET_LIVE_STREAM, teacher token, keyLive are correct';

        try {
            if (!TOKEN) return {} as EventSource;
            const eventSource = new EventSource(`${URL}`);

            eventSource.onmessage = (event) => {
                try {
                    const data: ILive = JSON.parse(event.data);
                    onMessage(data); // repassa para o callback
                } catch (parseError) {
                    console.error('Erro ao parsear mensagem SSE:', parseError);
                }
            };

            eventSource.onerror = (error) => {
                eventSource.close();
                if (onError) {
                    onError(error);
                } else {
                    throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
                }
            };

            return eventSource;
        } catch (error) {
            if (error instanceof GlobalError) error.logError();
            new GlobalError(ErrorType.UNEXPECTED_ERROR, GLOBAL_ERROR_UNEXPECT_DETAIL, GLOBAL_ERROR_UNEXPECT_ACTION, error).logError();
            throw error;
        }
    }

    protected async teacherPatchOperations(URL: string, TOKEN: IToken, SCOPE: IScope, keyLive: string, ERROR_DETAIL: string, ERROR_ACTION: string) {
        return await
            fetch(
                URL,
                {
                    method: 'PATCH',
                    headers: {
                        'Authorization': 'Bearer ' + TOKEN?.token
                    },
                    body: this.teacherOperationsLiveFormData(SCOPE.login, SCOPE.code, keyLive)
                }).catch(error => {
                throw new GlobalError(ErrorType.CONNECTION_FAILED, ERROR_DETAIL, ERROR_ACTION, error);
            });
    }

    protected createLiveFormData(login: string, code: string, quizKey: string) {
        let formData = new FormData();
        formData.append(GLOBAL_LOGIN, login);
        formData.append(GLOBAL_CODE, code);
        formData.append(GLOBAL_KEY_QUIZ, quizKey);
        return formData;
    }

    protected teacherOperationsLiveFormData(login: string, code: string, keyLive: string) {
        let formData = new FormData();
        formData.append(GLOBAL_LOGIN, login);
        formData.append(GLOBAL_CODE, code);
        formData.append(GLOBAL_KEY_LIVE, keyLive);
        return formData;
    }

    protected removePupilToLiveFormData(login: string, code: string, pupilLogin: string, pupilCode: string, keyLive: string) {
        let formData = new FormData();
        formData.append(GLOBAL_LOGIN, login);
        formData.append(GLOBAL_CODE, code);
        formData.append(GLOBAL_PUPIL_LOGIN, pupilLogin);
        formData.append(GLOBAL_PUPIL_CODE, pupilCode);
        formData.append(GLOBAL_KEY_LIVE, keyLive);
        return formData;
    }

    protected pupilOperationsToLiveFormData(pupilLogin: string, pupilCode: string, keyLive: string) {
        let formData = new FormData();
        formData.append(GLOBAL_PUPIL_LOGIN, pupilLogin);
        formData.append(GLOBAL_PUPIL_CODE, pupilCode);
        formData.append(GLOBAL_KEY_LIVE, keyLive);
        return formData;
    }

    protected liveBuildURLWithQueryParams(answerItem: string[] | undefined, URL: string) {
        const params = new URLSearchParams();
        if (answerItem && answerItem.length > 0) {
            answerItem.forEach(item => {
                params.append(GLOBAL_ANSWER_ITEM, item);
            });
        }
        const queryString = params.toString();
        return queryString ? `${URL}?${queryString}` : URL;
    }
}