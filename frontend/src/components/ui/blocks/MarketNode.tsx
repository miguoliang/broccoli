import GeneralNode from "./GeneralNode";
import { Grid } from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { memo } from "react";
import { ConcreteNodeDataProps } from "./types";
import PropertyUnit from "./PropertyUnit";

type MarketNodeProps = ConcreteNodeDataProps<"market">;
const MarketNode = (props: MarketNodeProps) => {
  const { t } = useTranslation();
  return (
    <GeneralNode {...props}>
      <Grid templateColumns={"1fr 3fr"} gap={1}>
        <PropertyUnit title={t("chinese")} key={"chinese"} value={""} />
      </Grid>
    </GeneralNode>
  );
};

export default memo(MarketNode);
