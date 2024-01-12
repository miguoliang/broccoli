import dayjs from "dayjs";
import utc from "dayjs/plugin/utc";
import timezone from "dayjs/plugin/timezone";

dayjs.extend(utc);
dayjs.extend(timezone);

export { default as appConfig } from "./app";
export { default as chakraTheme } from "./chakra";
export { default as echarts } from "./echarts";
export type { ECLineOption } from "./echarts";
export { default as navigationConfig } from "./navigation";
export type { NavigationMenuItem } from "./navigation";
export * from "./oidc";
export * from "./routes";
