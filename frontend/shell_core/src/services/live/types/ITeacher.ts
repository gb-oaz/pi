interface Control {
    currentPosition: number;
}

export interface ITeacher {
    login: string;
    code: string;
    control: Control
}