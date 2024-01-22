import { Card, CardHeader } from "@chakra-ui/react";
import { Handle, NodeProps, Position } from "reactflow";
import { GeneralNodeProps } from "./types";
import CustomNodeHeader from "./CustomNodeHeader";

const OriginNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <>
      <Card size={"xs"} colorScheme={"yellow"}>
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

export default OriginNode