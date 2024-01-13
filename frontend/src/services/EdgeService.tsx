import { Edge } from "models";
import { createQuery } from "react-query-kit";
import AxiosFetch from "./AxiosFetch";
import {
  StatParams,
  StatResponse,
  statResponseToResult,
  StatResult,
} from "./common";

export const useGetEdgesByVertices = createQuery<Edge[], string[]>({
  primaryKey: "get.edges-by-vertices",
  queryFn: ({ queryKey: [, data] }) =>
    AxiosFetch<Edge[]>({
      url: "/edges-by-vertices",
      method: "post",
      data,
    }).then((resp) => resp.data),
});

export const useGetEdgesStat = createQuery<StatResult, StatParams>({
  primaryKey: "get.edges.stat",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<StatResponse>({
      url: "/edges/stat",
      method: "get",
      params,
    }).then((resp) => statResponseToResult(resp.data)),
});
