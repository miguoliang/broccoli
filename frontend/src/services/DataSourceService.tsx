import {
  DataSource,
  PaginationRequest,
  PaginationResponse,
  Vertex,
} from "../models";
import { createQuery } from "react-query-kit";
import AxiosFetch from "./AxiosFetch";
import dayjs from "dayjs";
import { CountResponse } from "./common";

type GetDataSourcesParams = {
  date?: Date;
} & PaginationRequest;

type GetDataSourcesResponse = PaginationResponse<DataSource>;

export const useGetDataSources = createQuery<
  GetDataSourcesResponse,
  GetDataSourcesParams
>({
  primaryKey: "get.data-sources",
  queryFn: ({ queryKey: [, { date, ...rest }] }) =>
    AxiosFetch<GetDataSourcesResponse>({
      url: "/data-sources",
      method: "get",
      params: {
        date: dayjs(date).format("YYYY-MM-DD"),
        ...rest,
      },
    }).then((resp) => resp.data),
});

export const useGetVerticesByDataSource = createQuery<Vertex[], string>({
  primaryKey: "get.data-sources.vertices",
  queryFn: ({ queryKey: [, id] }) =>
    AxiosFetch<Vertex[]>({
      url: `/data-sources/${id}/vertices`,
      method: "get",
    }).then((resp) => resp.data),
});

export const useGetDataSourceCount = createQuery({
  primaryKey: "get.data-sources.count",
  queryFn: () =>
    AxiosFetch<CountResponse>({
      url: "/data-sources/count",
      method: "get",
    }).then((resp) => resp.data),
});
