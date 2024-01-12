import { appConfig } from "configs";
import { http, HttpResponse } from "msw";
import times from "lodash-es/times";
import dayjs from "dayjs";
import { faker } from "@faker-js/faker";

export default [
  http.get(`${appConfig.apiPrefix}/knowledge/history`, ({ request }) => {
    const url = new URL(request.url.toString());
    return HttpResponse.json(
      randomKnowledgeStatData(Number(url.searchParams.get("days"))),
    );
  }),
  http.get(`${appConfig.apiPrefix}/storage/history`, ({ request }) => {
    const url = new URL(request.url.toString());
    return HttpResponse.json(
      randomStorageStatData(Number(url.searchParams.get("months"))),
    );
  }),
  http.get(`${appConfig.apiPrefix}/network/history`, ({ request }) => {
    const url = new URL(request.url.toString());
    return HttpResponse.json(
      randomNetworkStatData(Number(url.searchParams.get("months"))),
    );
  }),
  http.get(`${appConfig.apiPrefix}/compute/history`, ({ request }) => {
    const url = new URL(request.url.toString());
    return HttpResponse.json(
      randomComputeStatData(Number(url.searchParams.get("months"))),
    );
  }),
  http.get(`${appConfig.apiPrefix}/data-source/history`, ({ request }) => {
    const url = new URL(request.url.toString());
    return HttpResponse.json(
      randomDataSourceStatData(Number(url.searchParams.get("months"))),
    );
  }),
];

function randomDataSourceStatData(months: number = 30) {
  const file = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  const website = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  const database = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  return { file, website, database };
}

function randomComputeStatData(months: number = 30) {
  const hours = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  return { hours };
}

function randomNetworkStatData(months: number = 30) {
  const read = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  const write = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  return { read, write };
}

function randomStorageStatData(months: number = 30) {
  const database = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  const file = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.int({ min: 500, max: 50000 }),
  }));
  const usage = times(months, (i) => ({
    date: dayjs().subtract(i, "month").format("YYYY-MM"),
    value: faker.number.float({ min: 0, max: 1 }),
  }));
  return { database, file, usage };
}

function randomKnowledgeStatData(days: number = 30) {
  const vertices = times(days, (i) => ({
    date: dayjs().subtract(i, "day").format("YYYY-MM-DD"),
    value: faker.number.int({ min: 1000, max: 10000 }),
  }));
  const edges = times(days, (i) => ({
    date: dayjs().subtract(i, "day").format("YYYY-MM-DD"),
    value: faker.number.int({ min: 1000, max: 10000 }),
  }));
  const properties = times(days, (i) => ({
    date: dayjs().subtract(i, "day").format("YYYY-MM-DD"),
    value: faker.number.int({ min: 1000, max: 10000 }),
  }));
  return { vertices, edges, properties };
}
