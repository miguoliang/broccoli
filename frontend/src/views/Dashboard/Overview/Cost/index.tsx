import { Box, Tab, TabList, TabPanel, TabPanels, Tabs } from "@chakra-ui/react";
import React from "react";
import CostExplorer from "./CostExplorer";

const CostCharts = () => {
  return (
    <Box borderRadius={"xl"} borderWidth={1} w={"full"} h={"full"}>
      <Tabs isLazy display={"flex"} flexDirection={"column"} h={"full"}>
        <TabList>
          <Tab py={3} fontSize={"sm"} fontWeight={"semibold"}>
            Cost Explorer
          </Tab>
          <Tab py={3} fontSize={"sm"} fontWeight={"semibold"}>
            Billings
          </Tab>
        </TabList>
        <TabPanels position={"relative"} flexGrow={1}>
          <TabPanel p={0} h={"full"}>
            <CostExplorer w={"full"} h={"full"} />
          </TabPanel>
          <TabPanel p={0} h={"full"}>
            dddd
          </TabPanel>
        </TabPanels>
      </Tabs>
    </Box>
  );
};

export default CostCharts;
