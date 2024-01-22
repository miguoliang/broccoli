import { Card, CardHeader } from "@chakra-ui/react";
import { GeneralNodeProps } from "./types";
import { Handle, NodeProps, Position } from "reactflow";
import CustomNodeHeader from "./CustomNodeHeader";

const ManufacturerNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <>
      <Card size={"xs"}>
        <CardHeader>
          <CustomNodeHeader {...props} />
        </CardHeader>
      </Card>
      <Handle type="source" position={Position.Top} id="a" />
      <Handle type="source" position={Position.Right} id="b" />
      <Handle type="source" position={Position.Bottom} id="c" />
      <Handle type="source" position={Position.Left} id="d" />
    </>
  );
};

export default ManufacturerNode