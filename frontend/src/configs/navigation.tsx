import React from "react";
import { USER_SCOPE } from "./oidc";

export type NavigationMenuItem = {
  key: string;
  path?: string;
  title: string;
  translateKey?: string;
  icon?: React.ReactElement;
  type: "item" | "title" | "collapse";
  authority: string[];
  children?: NavigationMenuItem[];
  parentKey?: string;
};

const navigation: NavigationMenuItem[] = [
  {
    key: "apps",
    title: "APPS",
    type: "title",
    authority: [USER_SCOPE],
  },
];

export default navigation;
