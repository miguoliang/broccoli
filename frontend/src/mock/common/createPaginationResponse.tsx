import { PaginationResponse } from "models";

function createPaginationResponse<T>(
  data: T[],
  page: number = 0,
  size: number = 10,
): PaginationResponse<T> {
  const content = data.slice(page * size, (page + 1) * size);
  return {
    content,
    pageable: {
      sort: [],
      offset: page * size,
      pageSize: size,
      pageNumber: page,
      paged: true,
      unpaged: false,
    },
    last: page === Math.ceil(data.length / size) - 1,
    totalElements: data.length,
    totalPages: Math.ceil(data.length / size),
    size,
    number: page,
    numberOfElements: content.length,
    first: page === 0,
    empty: content.length === 0,
    sort: [],
  };
}

export default createPaginationResponse;
