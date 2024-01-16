import { setupWorker } from "msw/browser";
import * as handlers from "gens/backend/api.msw";
import { HttpHandler } from "msw";

const mock = Object.values(handlers)
  .map((handler) => handler() as HttpHandler[])
  .reduce((acc, handler) => acc.concat(handler), []);

export const worker = setupWorker(...mock);
