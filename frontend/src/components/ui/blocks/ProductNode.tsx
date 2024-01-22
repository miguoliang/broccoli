import { Handle, NodeProps, Position } from "reactflow";
import { Card, CardHeader } from "@chakra-ui/react";
import CustomNodeHeader from "./CustomNodeHeader";
import { GeneralNodeProps } from "./types";

const ProductNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <>
      <Card size={"xs"} colorScheme={"blue"}>
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

export default ProductNode;
