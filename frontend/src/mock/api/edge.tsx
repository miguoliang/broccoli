import random from "lodash-es/random";
import { combinations } from "mathjs";
import dayjs from "dayjs";
import times from "lodash-es/times";
import { http, HttpResponse, PathParams } from "msw";
import { appConfig } from "configs";
import { Vertices } from "../seed";
import { flatMap } from "lodash-es";

export default [
  http.post<PathParams<string>, string[], undefined>(
    `${appConfig.apiPrefix}/edges-by-vertices`,
    async ({ request }) => {
      const vertexIds = await request.json();
      const pairs = flatMap(vertexIds, (v, i) =>
        vertexIds.slice(i + 1).map((v2) => [v, v2]),
      );
      return HttpResponse.json(
        pairs.map(([v1, v2]) => ({
          inVertexId: v1,
          name: "associated with",
          outVertexId: v2,
        })),
      );
    },
  ),
  http.get(`${appConfig.apiPrefix}/edges/stat`, () => {
    const c = combinations(Vertices.length, 2);
    return HttpResponse.json(
      times(100, (i) => ({
        label: dayjs().subtract(i, "day").format("YYYY-MM-DD"),
        value: random(0, c),
      })),
    );
  }),
];
