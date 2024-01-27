import { GeneralNodeProps } from "./types";
import { Handle, NodeProps, Position } from "reactflow";

const ManufacturerNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <>
      <input autoFocus={true} />
      <Handle type="source" position={Position.Top} id="a" />
      <Handle type="source" position={Position.Right} id="b" />
      <Handle type="source" position={Position.Bottom} id="c" />
      <Handle type="source" position={Position.Left} id="d" />
    </>
  );
};

export default ManufacturerNode