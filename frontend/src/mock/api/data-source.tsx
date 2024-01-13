import createPaginationResponse from "../common/createPaginationResponse";
import shuffle from "lodash-es/shuffle";
import random from "lodash-es/random";
import { appConfig } from "configs";
import { http, HttpResponse } from "msw";
import { DataSources, Vertices } from "../seed";

export default [
  http.get(`${appConfig.apiPrefix}/data-sources`, ({ request }) => {
    const url = new URL(request.url.toString());
    const page = Number(url.searchParams.get("page") ?? "0");
    const size = Number(url.searchParams.get("size") ?? "10");
    return HttpResponse.json(createPaginationResponse(DataSources, page, size));
  }),
  http.get(`${appConfig.apiPrefix}/data-sources/count`, () =>
    HttpResponse.json({ count: DataSources.length }),
  ),
  http.get(`${appConfig.apiPrefix}/data-sources/:id/vertices`, () =>
    HttpResponse.json(shuffle(Vertices).splice(0, random(10, 20))),
  ),
];
