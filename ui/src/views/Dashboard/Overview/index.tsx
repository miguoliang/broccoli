import { GridItem, SimpleGrid } from "@chakra-ui/react";
import StatHeader from "./StatHeader";
import KnowledgeCharts from "./Knowledge";
import ResourceCharts from "./Resource";
import { PageHeader } from "../components";
import CostCharts from "./Cost";

const Overview = () => {
  return (
    <>
      <PageHeader
        title={"Overview"}
        description={"View your current knowledge & resources"}
      />
      <SimpleGrid columns={4} gap={5}>
        <GridItem colSpan={4} w={"full"}>
          <StatHeader />
        </GridItem>
        <GridItem colSpan={2} h={"450px"} position={"relative"}>
          <KnowledgeCharts />
        </GridItem>
        <GridItem colSpan={2} h={"450px"} position={"relative"}>
          <ResourceCharts />
        </GridItem>
        <GridItem colSpan={4} h={"450px"} position={"relative"}>
          <CostCharts />
        </GridItem>
      </SimpleGrid>
    </>
  );
};

export default Overview;
