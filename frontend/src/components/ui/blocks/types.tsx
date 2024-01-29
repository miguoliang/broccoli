import { GeneralNodeProps } from "./GeneralNode";

export type NodeType = "product" | "manufacturer" | "application" | "origin" | "market";

export type GeneralNodeDataProps = {
  id: string;
  name: string;
  type: NodeType;
}

export type ColorSchemes = Record<NodeType, string>;

export type ConcreteNodeDataProps<T = NodeType> = Omit<
  GeneralNodeProps,
  "children" | "data" | "type"
> & { type: T } & {
  data: Omit<GeneralNodeProps["data"], "type"> & { type: T };
};