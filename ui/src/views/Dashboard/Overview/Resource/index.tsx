import { Box, Tab, TabList, TabPanel, TabPanels, Tabs } from "@chakra-ui/react";
import StorageGrowthChart from "./StorageGrowthChart";
import React from "react";
import NetworkGrowthChart from "./NetworkGrowthChart";
import ComputeGrowthChart from "./ComputeGrowthChart";

const ResourceCharts = () => {
  return (
    <Box borderRadius={"xl"} borderWidth={1} w={"full"} h={"full"}>
      <Tabs isLazy display={"flex"} flexDirection={"column"} h={"full"}>
        <TabList>
          <Tab py={3} fontSize={"sm"} fontWeight={"semibold"}>
            Storage
          </Tab>
          <Tab py={3} fontSize={"sm"} fontWeight={"semibold"}>
            Network
          </Tab>
          <Tab py={3} fontSize={"sm"} fontWeight={"semibold"}>
            Compute
          </Tab>
        </TabList>
        <TabPanels position={"relative"} flexGrow={1}>
          <TabPanel p={0} h={"full"}>
            <StorageGrowthChart w={"full"} h={"full"} />
          </TabPanel>
          <TabPanel p={0} h={"full"}>
            <NetworkGrowthChart w={"full"} h={"full"} />
          </TabPanel>
          <TabPanel p={0} h={"full"}>
            <ComputeGrowthChart w={"full"} h={"full"} />
          </TabPanel>
        </TabPanels>
      </Tabs>
    </Box>
  );
};

export default ResourceCharts;
