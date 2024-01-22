import { Card, CardHeader } from "@chakra-ui/react";
import { NodeProps } from "reactflow";
import { GeneralNodeProps } from "./types";
import CustomNodeHeader from "./CustomNodeHeader";

const MarketNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <Card size={"xs"} colorScheme={"green"}>
      <CardHeader>
        <CustomNodeHeader {...props} />
      </CardHeader>
    </Card>
  );
};

export default MarketNode