export type Graph = {
  nodes?: Node[];
  links?: Link[];
  categories?: Category[];
};

export type Category = {
  name: string;
};

export type Link = {
  source: string;
  target: string;
};

export type Node = {
  id: string;
  name: string;
  symbolSize?: number;
  x?: number;
  y?: number;
  value?: number;
  category?: number;
};
