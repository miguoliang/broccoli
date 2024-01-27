import React, { LazyExoticComponent } from "react";
import { HTMLChakraProps } from "@chakra-ui/react";

type AppRouteProps = {
  component: LazyExoticComponent<(props: HTMLChakraProps<"div">) => JSX.Element>;
} & HTMLChakraProps<"div">;

export const AppRoute = ({
  component: Component,
  ...props
}: AppRouteProps) => {
  return <Component {...props} />;
};
