import { create } from "zustand";
import {
  addEdge,
  applyEdgeChanges,
  applyNodeChanges,
  Connection,
  Edge,
  EdgeChange,
  Node,
  NodeChange,
  OnConnect,
  OnEdgesChange,
  OnNodesChange,
} from "reactflow";
import { GeneralNodeProps } from "../components/ui/blocks";
import { sha512 } from "../commons/sha512";

type RFState = {
  nodes: Node[];
  edges: Edge[];
  setNodes: (nodes: Node[]) => void;
  setEdges: (edges: Edge[]) => void;
  onNodesChange: OnNodesChange;
  onEdgesChange: OnEdgesChange;
  onConnect: OnConnect;
  setNodeData: (id: string, data: GeneralNodeProps) => Promise<void>;
};

// this is our useStore hook that we can use in our components to get parts of the store and call actions
const useStore = create<RFState>((set, get) => ({
  nodes: [],
  edges: [],
  onNodesChange: (changes: NodeChange[]) => {
    set({
      nodes: applyNodeChanges(changes, get().nodes),
    });
  },
  onEdgesChange: (changes: EdgeChange[]) => {
    set({
      edges: applyEdgeChanges(changes, get().edges),
    });
  },
  onConnect: (connection: Connection) => {
    set({
      edges: addEdge(connection, get().edges),
    });
  },
  setNodes: (nodes: Node[]) => set({ nodes }),
  setEdges: (edges: Edge[]) => set({ edges }),
  setNodeData: async (id: string, data: GeneralNodeProps) => {
    const newId = await sha512(`${data.type}_${data.name}`);
    const nodes = get().nodes.map((node) => {
      if (node.id === id) {
        data.id = newId;
        node.id = newId;
        return {
          ...node,
          data,
        };
      }
      return node;
    });
    set({ nodes });
  },
}));

export default useStore;
