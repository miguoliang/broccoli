import { Vertex } from "models";
import createPaginationResponse from "../common/createPaginationResponse";
import random from "lodash-es/random";
import times from "lodash-es/times";
import dayjs from "dayjs";
import { appConfig } from "configs";
import { http, HttpResponse } from "msw";
import { Vertices } from "mock/seed";

export default [
  http.get(`${appConfig.apiPrefix}/vertices`, ({ request }) => {
    const url = new URL(request.url.toString());
    const q = url.searchParams.get("q")?.toLowerCase();
    const size = Number(url.searchParams.get("size") ?? "10");
    const vertices = Vertices.filter(
      (v: Vertex) => q && v.name.toLowerCase().includes(q),
    );
    return HttpResponse.json(createPaginationResponse(vertices.slice(0, size)));
  }),

  http.get(`${appConfig.apiPrefix}/vertices/stat`, () =>
    HttpResponse.json(
      times(100, (i) => ({
        label: dayjs().subtract(i, "day").format("YYYY-MM-DD"),
        value: random(0, 1000),
      })),
    ),
  ),
];
