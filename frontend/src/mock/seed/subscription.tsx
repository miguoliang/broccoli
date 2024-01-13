import times from "lodash-es/times";
import { Subscription } from "models";
import { faker } from "@faker-js/faker";
import random from "lodash-es/random";

export const subscriptions: Subscription[] = times(100, () => ({
  subscriptionId: faker.string.uuid(),
  email: faker.internet.email(),
  planId: "basic",
  planDescription: "Basic Plan",
  planName: "Basic",
  planAmount: 9.99,
  startTime: "2021-01-01",
  endTime: "2021-02-01",
  status: random(0, 1) === 0 ? "active" : "inactive",
}));
