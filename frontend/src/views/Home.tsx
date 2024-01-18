import ReactFlow, { Background, Controls, MiniMap } from "reactflow";
import { Box } from "@chakra-ui/react";

const Home = () => {
  return (
    <Box h={"500px"}>
      <ReactFlow proOptions={{ hideAttribution: true }}>
        <Background />
        <Controls />
        <MiniMap />
      </ReactFlow>
    </Box>
  );
};

export default Home;
