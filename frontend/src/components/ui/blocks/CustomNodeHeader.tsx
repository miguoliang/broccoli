import { Badge, VStack } from "@chakra-ui/react";
import CustomEditable from "./CustomEditable";
import { GeneralNodeProps } from "./types";
import { useTranslation } from "react-i18next";
import { NodeProps } from "reactflow";

const CustomNodeHeader = ({ type, data }: NodeProps<GeneralNodeProps>) => {
  const { t } = useTranslation();
  return (
    <VStack gap={1}>
      <Badge fontSize={"xs"}>{t(type)}</Badge>
      <CustomEditable defaultValue={data.name} />
    </VStack>
  );
};

export default CustomNodeHeader;
