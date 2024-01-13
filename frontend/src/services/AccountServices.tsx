import { Subscription } from "models";
import AxiosFetch from "./AxiosFetch";
import { createQuery } from "react-query-kit";
import noop from "lodash-es/noop";
import { TimeSeries } from "./common";

type PostAccountChangePasswordRequestBody = {
  previousPassword: string;
  proposedPassword: string;
};

export const usePostAccountChangePassword = createQuery<
  unknown,
  PostAccountChangePasswordRequestBody
>({
  primaryKey: "post.account.change-password",
  queryFn: ({ queryKey: [, data] }) =>
    AxiosFetch({
      url: "/account/change-password",
      method: "post",
      data,
    }).then(noop),
});

export const useGetAccountSubscriptions = createQuery({
  primaryKey: "get.account.subscriptions",
  queryFn: () =>
    AxiosFetch<Subscription[]>({
      url: `/account/subscriptions`,
      method: "get",
    }).then((resp) => resp.data),
});

export type UseGetAccountCostResponse = {
  series: TimeSeries;
  usages: { label: string; amount: number }[];
  previous: number;
  current: number;
  forecast: number;
};

export const useGetAccountCost = createQuery({
  primaryKey: "get.account.cost",
  queryFn: () =>
    AxiosFetch<UseGetAccountCostResponse>({
      url: `/account/cost`,
      method: "get",
    }).then((resp) => resp.data),
});
