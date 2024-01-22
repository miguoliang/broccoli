import { NodeProps } from "reactflow";
import { Card, CardHeader } from "@chakra-ui/react";
import CustomNodeHeader from "./CustomNodeHeader";
import { GeneralNodeProps } from "./types";

const ProductNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <Card size={"xs"} colorScheme={"blue"}>
      <CardHeader>
        <CustomNodeHeader {...props} />
      </CardHeader>
    </Card>
  );
};

export default ProductNode;
