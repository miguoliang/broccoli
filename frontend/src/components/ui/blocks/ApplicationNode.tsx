import { Card, CardHeader } from "@chakra-ui/react";
import { NodeProps } from "reactflow";
import { GeneralNodeProps } from "./types";
import CustomNodeHeader from "./CustomNodeHeader";

const ApplicationNode = (props: NodeProps<GeneralNodeProps>) => {
  return (
    <Card size={"xs"} colorScheme={"red"}>
      <CardHeader>
        <CustomNodeHeader {...props} />
      </CardHeader>
    </Card>
  );
};

export default ApplicationNode