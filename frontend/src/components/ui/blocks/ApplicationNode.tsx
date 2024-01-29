import GeneralNode from "./GeneralNode";
import { Grid, Text } from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { memo } from "react";
import { ConcreteNodeDataProps } from "./types";

type ApplicationNodeProps = ConcreteNodeDataProps<"application">;
const ApplicationNode = (props: ApplicationNodeProps) => {
  const { t } = useTranslation();
  return (
    <GeneralNode {...props}>
      <Grid templateColumns={"1fr 2.5fr"} gap={1}>
        <Text>{t("chinese")}</Text>
        <input />
        <Text>{t("japanese")}</Text>
        <input />
      </Grid>
    </GeneralNode>
  );
};

export default memo(ApplicationNode);
