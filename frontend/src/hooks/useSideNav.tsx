import { create } from "zustand";

interface SideNavStore {
  collapsed: boolean;
  setCollapsed: (collapsed: boolean) => void;
  expandedKeys: Set<string>;
  setExpandedKeys: (expandedKeys: Set<string>) => void;
  addExpandedKey: (expandedKey: string) => void;
  removeExpandedKey: (expandedKey: string) => void;
}

export const useSideNav = create<SideNavStore>((set) => ({
  collapsed: false,
  setCollapsed: (collapsed: boolean) => set({ collapsed }),
  expandedKeys: new Set<string>(),
  setExpandedKeys: (expandedKeys: Set<string>) => set({ expandedKeys }),
  addExpandedKey: (expandedKey: string) =>
    set((state) => {
      const expandedKeys = new Set(state.expandedKeys);
      expandedKeys.add(expandedKey);
      console.log("after add: ", expandedKeys);
      return { expandedKeys };
    }),
  removeExpandedKey: (expandedKey: string) =>
    set((state) => {
      const expandedKeys = new Set(state.expandedKeys);
      expandedKeys.delete(expandedKey);
      console.log("after delete: ", expandedKeys);
      return { expandedKeys };
    }),
}));
