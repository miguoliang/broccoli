import React from "react";
import { USER_SCOPE } from "./oidc";
import { Icon } from "@chakra-ui/react";
import { HiOutlineComputerDesktop } from "react-icons/hi2";
import { HiOutlineShare, HiOutlineUsers } from "react-icons/hi";

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
  {
    key: "dashboard",
    path: "/dashboard/overview",
    title: "Overview",
    icon: <Icon as={HiOutlineComputerDesktop} />,
    type: "item",
    authority: [USER_SCOPE],
  },
  {
    key: "dashboard.data-source",
    path: "/dashboard/data-sources",
    title: "Data Source",
    icon: <Icon as={HiOutlineUsers} />,
    type: "item",
    authority: [USER_SCOPE],
  },
  {
    key: "dashboard.graph",
    path: "/dashboard/graph",
    title: "Graph",
    icon: <Icon as={HiOutlineShare} />,
    type: "item",
    authority: [USER_SCOPE],
  },
];

export default navigation;
