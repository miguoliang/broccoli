export type StatResponse<T = number> = { label: string; value: T }[];

export type StatParams =
  | {
      start?: Date;
      end?: Date;
      period?: StatePeriod;
    }
  | null
  | undefined;

export type StatePeriod = "day" | "week" | "month" | "year";

export type StatResult = {
  label: string;
  value: number;
  changed: number;
  percentage: number;
}[];

export type TimeSeries<T = number> = {
  date: string;
  value: T;
}[];

export type CountResponse = {
  count: number;
};

export function statResponseToResult(statResponse: StatResponse): StatResult {
  return statResponse
    .map(({ label, value }, index) => {
      const changed = index === 0 ? 0 : value - statResponse[index - 1].value;
      const percentage =
        index === 0 ? 0 : (changed / statResponse[index - 1].value) * 100;
      return { label, value, changed, percentage };
    })
    .reverse();
}
