import {
  accountApi,
  dataSourceApi,
  edgeApi,
  miscApi,
  propertyApi,
  vertexApi,
} from "./api";
import { setupWorker } from "msw/browser";

const handlers = [
  ...accountApi,
  ...dataSourceApi,
  ...edgeApi,
  ...propertyApi,
  ...vertexApi,
  ...miscApi,
];

export const worker = setupWorker(...handlers);
