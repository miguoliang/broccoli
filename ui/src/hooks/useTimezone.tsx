import { create } from "zustand";
import dayjs from "dayjs";

interface TimezoneState {
  timezone: string;
  setTimezone: (timezone: string) => void;
}

export const useTimezone = create<TimezoneState>((set) => ({
  timezone: "UTC",
  setTimezone: (timezone: string) => {
    dayjs.tz.setDefault(timezone);
    set({ timezone });
  },
}));
