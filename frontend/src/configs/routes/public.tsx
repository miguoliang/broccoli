import React from "react";
import { Routes } from "./types";

const publicRoutes: Routes = [
  {
    key: "home",
    path: "/",
    component: React.lazy(() => import("views/Home")),
  },
  {
    key: "accessDenied",
    path: "/access-denied",
    component: React.lazy(() => import("views/AccessDenied")),
  },
];

export default publicRoutes;
