import { createQuery } from "react-query-kit";
import AxiosFetch from "./AxiosFetch";
import {
  StatParams,
  StatResponse,
  statResponseToResult,
  StatResult,
} from "./common";

export const useGetPropertiesStat = createQuery<StatResult, StatParams>({
  primaryKey: "get.properties.stat",
  queryFn: ({ queryKey: [, params] }) =>
    AxiosFetch<StatResponse>({
      url: "/properties/stat",
      method: "get",
      params,
    }).then((res) => statResponseToResult(res.data)),
});
