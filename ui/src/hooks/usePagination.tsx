import { PaginationState } from "@tanstack/react-table";
import { useState } from "react";

export const usePagination = () =>
  useState<PaginationState>({
    pageIndex: 0,
    pageSize: 10,
  });
