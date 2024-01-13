import { PaginationState } from "@tanstack/react-table";
import { useState } from "react";

export const usePagination = () => {
  const [paginationState, setPaginationState] = useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });
  return [paginationState, setPaginationState];
};
