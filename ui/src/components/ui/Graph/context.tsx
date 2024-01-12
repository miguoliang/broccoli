import noop from "lodash-es/noop";
import { Edge as EdgeModel, Vertex as VertexModel } from "models";
import { createContext, useState } from "react";

interface GraphContextProps {
  vertices: VertexModel[];
  edges: EdgeModel[];
  setVertices: (vertices: VertexModel[]) => void;
  setEdges: (edges: EdgeModel[]) => void;
}

export const GraphContext = createContext<GraphContextProps>({
  vertices: [],
  edges: [],
  setVertices: noop,
  setEdges: noop,
});

type UseGraphContextProps = {
  initialVertices?: VertexModel[];
  initialEdges?: EdgeModel[];
};

export const useGraphContext = ({
  initialVertices = [],
  initialEdges = [],
}: UseGraphContextProps): GraphContextProps => {
  const [vertices, setVertices] = useState(initialVertices);
  const [edges, setEdges] = useState(initialEdges);
  return {
    vertices,
    edges,
    setVertices,
    setEdges,
  };
};
