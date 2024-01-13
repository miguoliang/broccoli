import random from "lodash-es/random";
import times from "lodash-es/times";
import dayjs from "dayjs";
import { http, HttpResponse } from "msw";
import { appConfig } from "configs";

export default [
  http.get(`${appConfig.apiPrefix}/properties/stat`, () =>
    HttpResponse.json(
      times(100, (i) => ({
        label: dayjs().subtract(i, "day").format("YYYY-MM-DD"),
        value: random(0, 1000),
      })),
    ),
  ),
];
