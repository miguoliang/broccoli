import { Card, CardHeader } from "@chakra-ui/react";
import { GeneralNodeProps } from "./types";
import { NodeProps } from "reactflow";
import CustomNodeHeader from "./CustomNodeHeader";

const ManufacturerNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <Card size={"xs"}>
      <CardHeader>
        <CustomNodeHeader {...props} />
      </CardHeader>
    </Card>
  );
};

export default ManufacturerNode