import ReactFlow, { Background, Controls, MiniMap, Panel } from "reactflow";
import { useSizes } from "@chakra-ui/react-use-size";
import { useMemo } from "react";
import { Box, Button, Icon, useDisclosure } from "@chakra-ui/react";
import { FaPlus } from "react-icons/fa6";
import { useTranslation } from "react-i18next";
import BlockLibraryModal from "./BlockLibraryModal";

const Home = () => {
  const { t } = useTranslation();

  const { isOpen, onOpen, onClose } = useDisclosure();

  const [bodySize, headerSize] = useSizes({
    getNodes: () => [
      document.querySelector("div#root") as HTMLElement,
      document.querySelector("header"),
    ],
    observeMutation: true,
  });

  const reactFlowHeight = useMemo(() => {
    if (!bodySize || !headerSize) {
      return "100%";
    }
    return bodySize.height - headerSize.height;
  }, [bodySize, headerSize]);

  return (
    <>
      <Box h={reactFlowHeight}>
        <ReactFlow proOptions={{ hideAttribution: true }}>
          <Background />
          <Controls />
          <MiniMap />
          <Panel position={"top-left"}>
            <Button
              size={"sm"}
              leftIcon={<Icon as={FaPlus} size={"sm"} />}
              onClick={onOpen}
            >
              {t("block")}
            </Button>
          </Panel>
        </ReactFlow>
      </Box>
      <BlockLibraryModal isOpen={isOpen} onClose={onClose} />
    </>
  );
};

export default Home;
