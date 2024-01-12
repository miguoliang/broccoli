export type PaginationRequest = {
  page?: number;
  size?: number;
  sort?: string;
};

export type PaginationResponse<T = unknown> = {
  content: T[];
  empty: boolean;
  first: boolean;
  last: boolean;
  number: number;
  numberOfElements: number;
  totalElements: number;
  totalPages: number;
  size: number;
  pageable: Pageable;
  sort: Sort[];
};

type Pageable = {
  offset: number;
  pageSize: number;
  pageNumber: number;
  paged: boolean;
  unpaged: boolean;
  sort: Sort[];
};

type Sort = {
  direction: "ASC" | "DESC";
  property: string;
  ignoreCase: boolean;
  nullHandling: "NATIVE" | "NULLS_FIRST" | "NULLS_LAST";
  ascending: boolean;
  descending: boolean;
};
