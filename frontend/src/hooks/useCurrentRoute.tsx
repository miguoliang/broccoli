import { create } from "zustand";

interface CurrentRouteStore {
  key: string;
  setKey: (key: string) => void;
}

export const useCurrentRoute = create<CurrentRouteStore>((set) => ({
  key: "/",
  setKey: (key) => set({ key }),
}));
