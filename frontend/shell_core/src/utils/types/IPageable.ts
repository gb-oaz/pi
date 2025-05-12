export interface IPagination {
    page: number;
    size: number;
    total: number;
}

export interface IPageable<T> {
    content: T[];
    pagination: IPagination;
}