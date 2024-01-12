import { HTMLChakraProps } from "@chakra-ui/react";
import { LazyExoticComponent } from "react";

export type Route = {
  path: string;
  key: string;
  component: LazyExoticComponent<
    (props: HTMLChakraProps<"div">) => JSX.Element
  >;
  authority?: string[];
  label?: string;
  meta?: object;
};

export type Routes = Route[];
