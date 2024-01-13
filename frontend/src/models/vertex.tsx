import { DataSource } from "./data-source";
import { Edge } from "./edge";

export interface Vertex {
  id: string;
  name: string;
  type: string;
  status: string;
  dataSources?: DataSource[];
  inEdges?: Edge[];
  outEdges?: Edge[];
}
