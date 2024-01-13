import React, { lazy } from "react";
import { Tab, TabList, TabPanel, TabPanels, Tabs } from "@chakra-ui/react";
import { PageHeader } from "../components";

const Password = lazy(() => import("./Password"));
const Subscription = lazy(() => import("./Subscription"));

const settingsMenu = [
  { label: "Password", path: "password" },
  { label: "Subscription", path: "subscription" },
];

const Settings = () => {
  return (
    <>
      <PageHeader
        title={"Account"}
        description={"Manage your account settings"}
      />
      <Tabs variant={"enclosed"}>
        <TabList>
          {settingsMenu.map((it, index) => (
            <Tab key={it.path}>{it.label}</Tab>
          ))}
        </TabList>
        <TabPanels paddingY={2}>
          <TabPanel>
            <Password />
          </TabPanel>
          <TabPanel>
            <Subscription />
          </TabPanel>
        </TabPanels>
      </Tabs>
    </>
  );
};

export default Settings;
