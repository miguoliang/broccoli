import { setupWorker } from "msw/browser";
import { getBroccoliMock } from "gens/backend/api.msw";

export const worker = setupWorker(...getBroccoliMock());
