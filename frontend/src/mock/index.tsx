import { setupWorker } from "msw/browser";
import { RequestHandler } from "msw";

const handlers: RequestHandler[] = [];

export const worker = setupWorker(...handlers);
