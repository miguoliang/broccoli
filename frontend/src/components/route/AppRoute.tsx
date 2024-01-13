import React, { LazyExoticComponent, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { useCurrentRoute } from "hooks";
import { HTMLChakraProps } from "@chakra-ui/react";

type AppRouteProps = {
  component: LazyExoticComponent<
    (props: HTMLChakraProps<"div">) => JSX.Element
  >;
  routeKey: string;
} & HTMLChakraProps<"div">;

export const AppRoute = ({
  component: Component,
  routeKey,
  ...props
}: AppRouteProps) => {
  const location = useLocation();
  const currentRoute = useCurrentRoute();
  useEffect(() => {
    currentRoute.setKey(routeKey);
  }, [location]);
  return <Component {...props} />;
};
