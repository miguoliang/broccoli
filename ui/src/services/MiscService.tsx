import { TimeSeries } from "./common";
import { createQuery } from "react-query-kit";
import AxiosFetch from "./AxiosFetch";

interface KnowledgeHistoryParams {
  days?: number;
}

interface KnowledgeHistoryResponse {
  vertices: TimeSeries;
  edges: TimeSeries;
  properties: TimeSeries;
}

export const useGetKnowledgeHistory = createQuery<
  KnowledgeHistoryResponse,
  KnowledgeHistoryParams
>({
  primaryKey: "get.knowledge.history",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<KnowledgeHistoryResponse>({
      url: "/knowledge/history",
      method: "get",
      params,
    }).then((res) => res.data),
});

interface StorageHistoryParams {
  months?: number;
}

interface StorageHistoryResponse {
  database: TimeSeries;
  file: TimeSeries;
  usage: TimeSeries;
}

export const useGetStorageHistory = createQuery<
  StorageHistoryResponse,
  StorageHistoryParams
>({
  primaryKey: "get.storage.history",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<StorageHistoryResponse>({
      url: "/storage/history",
      method: "get",
      params,
    }).then((res) => res.data),
});

interface NetworkHistoryParams {
  months?: number;
}

interface NetworkHistoryResponse {
  read: TimeSeries;
  write: TimeSeries;
}

export const useGetNetworkHistory = createQuery<
  NetworkHistoryResponse,
  NetworkHistoryParams
>({
  primaryKey: "get.network.history",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<NetworkHistoryResponse>({
      url: "/network/history",
      method: "get",
      params,
    }).then((res) => res.data),
});

interface ComputeHistoryParams {
  months?: number;
}

interface ComputeHistoryResponse {
  hours: TimeSeries;
}

export const useGetComputeHistory = createQuery<
  ComputeHistoryResponse,
  ComputeHistoryParams
>({
  primaryKey: "get.compute.history",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<ComputeHistoryResponse>({
      url: "/compute/history",
      method: "get",
      params,
    }).then((res) => res.data),
});

interface DataSourceHistoryParams {
  months?: number;
}

interface DataSourceHistoryResponse {
  database: TimeSeries;
  file: TimeSeries;
  website: TimeSeries;
}

export const useGetDataSourceHistory = createQuery<
  DataSourceHistoryResponse,
  DataSourceHistoryParams
>({
  primaryKey: "get.data-source.history",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<DataSourceHistoryResponse>({
      url: "/data-source/history",
      method: "get",
      params,
    }).then((res) => res.data),
});
