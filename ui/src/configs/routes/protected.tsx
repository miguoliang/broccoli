import React from "react";
import { Routes } from "./types";

const protectedRoutes: Routes = [
  {
    key: "dashboard.overview",
    path: `/dashboard/overview`,
    component: React.lazy(() => import("views/Dashboard/Overview")),
  },
  {
    key: "dashboard.data-source",
    path: `/dashboard/data-sources`,
    component: React.lazy(() => import("views/Dashboard/DataSource")),
  },
  {
    key: "dashboard.graph",
    path: `/dashboard/graph`,
    component: React.lazy(() => import("views/Dashboard/Graph")),
  },
  {
    key: "dashboard.account",
    path: `/dashboard/account`,
    component: React.lazy(() => import("views/Dashboard/Account")),
  },
];

export default protectedRoutes;
