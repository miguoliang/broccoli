import { PaginationRequest, PaginationResponse, Vertex } from "models";
import { createQuery } from "react-query-kit";
import AxiosFetch from "./AxiosFetch";
import {
  StatParams,
  StatResponse,
  statResponseToResult,
  StatResult,
} from "./common";

type GetVerticesResponse = PaginationResponse<Vertex>;

type GetVerticesParams = {
  q: string;
} & PaginationRequest;

export const useGetVertices = createQuery<
  GetVerticesResponse,
  GetVerticesParams
>({
  primaryKey: "get.vertices",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<GetVerticesResponse>({
      url: "/vertices",
      method: "get",
      params,
    }).then((res) => res.data),
});

export const useGetVerticesStat = createQuery<StatResult, StatParams>({
  primaryKey: "get.vertices.stat",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<StatResponse>({
      url: "/vertices/stat",
      method: "get",
      params,
    }).then((res) => statResponseToResult(res.data)),
});
