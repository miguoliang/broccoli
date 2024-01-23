export type NodeType = "product" | "manufacturer" | "application" | "origin" | "market";

export type GeneralNodeProps = {
  id: string;
  name: string;
  type: NodeType;
}

export type ColorSchemes = Record<NodeType, string>;