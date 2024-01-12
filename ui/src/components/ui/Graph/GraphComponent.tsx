import { useContext, useEffect } from "react";
import {
  Background,
  Controls,
  Edge,
  MiniMap,
  Node,
  Position,
  ReactFlow,
  useReactFlow,
} from "reactflow";
import { Edge as EdgeModel, Vertex as VertexModel } from "models";
import { GraphContext } from "./context";
import FloatingEdge from "./FloatingEdge";
import FloatingConnectionLine from "./FloatingConnectionLine";
import "./GraphComponent.css";
import * as d3 from "d3";
import { SimulationLinkDatum, SimulationNodeDatum } from "d3-force";

type NodeDataType = {
  label: string;
};

const edgeTypes = {
  floating: FloatingEdge,
};

export const GraphComponent = () => {
  const { vertices, edges } = useContext(GraphContext);
  const { setNodes, setEdges } = useReactFlow();

  useEffect(() => {
    const nodes = vertices as SimulationNodeDatum[];
    const links = edges.map((e) => ({
      source: e.inVertexId,
      target: e.outVertexId,
      id: `${e.inVertexId}-${e.name}-${e.outVertexId}`,
    })) as SimulationLinkDatum<SimulationNodeDatum>[];
    d3.forceSimulation(nodes)
      .force(
        "link",
        d3
          .forceLink()
          .id((d) => (d as VertexModel).id)
          .links(links)
          .distance(500)
          .strength(1),
      )
      .force("center", d3.forceCenter())
      .on("end", () => {
        setNodes(vertices2nodes(vertices));
        setEdges(edges2links(edges));
      });
  }, [vertices, edges]);

  return (
    <ReactFlow
      defaultNodes={[]}
      defaultEdges={[]}
      edgeTypes={edgeTypes}
      connectionLineComponent={FloatingConnectionLine}
      className="graph-component"
      fitView
    >
      <Background />
      <Controls />
      <MiniMap />
    </ReactFlow>
  );
};

function vertices2nodes(vertices: VertexModel[]): Node<NodeDataType>[] {
  return vertices.map((v) => ({
    id: v.id,
    position: {
      x: (v as SimulationNodeDatum).x || 0,
      y: (v as SimulationNodeDatum).y || 0,
    },
    data: { label: v.name },
    targetPosition: Position.Right,
    sourcePosition: Position.Left,
  }));
}

function edges2links(edges: EdgeModel[]): Edge[] {
  return edges.map((e) => ({
    source: e.inVertexId,
    target: e.outVertexId,
    id: `${e.inVertexId}-${e.name}-${e.outVertexId}`,
    type: "floating",
  }));
}
