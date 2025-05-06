interface Answer {
    position: number;
    answer: string[];
    hit: boolean;
}

export interface IEvaluation {
    evaluation: Map<string, Set<Answer>>;
}