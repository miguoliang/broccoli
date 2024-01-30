import { ReactFlowInstance } from "reactflow";
import { create } from "zustand";
import { GeneralNodeDataProps } from "../components/ui/blocks";

type ReactFlowStoreProps = {
  instance?: ReactFlowInstance<GeneralNodeDataProps>;
};

const useReactFlowStore = create<ReactFlowStoreProps>(() => ({}));

export default useReactFlowStore;
