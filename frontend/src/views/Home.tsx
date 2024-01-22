import ReactFlow, { Background, Panel } from "reactflow";
import { useSizes } from "@chakra-ui/react-use-size";
import { useMemo } from "react";
import { Box, Button, Icon, useColorModeValue, useDisclosure } from "@chakra-ui/react";
import { FaPlus } from "react-icons/fa6";
import { useTranslation } from "react-i18next";
import BlockLibraryModal from "./BlockLibraryModal";
import "reactflow/dist/style.css";
import useStore from "hooks/useReactFlowState";
import { StyledControls, StyledMiniMap } from "../components/ui";
import ProductNode from "../components/ui/blocks/ProductNode";
import ManufacturerNode from "../components/ui/blocks/ManufacturerNode";
import ApplicationNode from "../components/ui/blocks/ApplicationNode";
import MarketNode from "../components/ui/blocks/MarketNode";
import OriginNode from "../components/ui/blocks/OriginNode";

const Home = () => {
  const { t } = useTranslation();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const { nodes, onNodesChange, edges, onConnect, onEdgesChange } = useStore();

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

  const minimapStyles = {
    "&.react-flow__minimap": {
      backgroundColor: useColorModeValue("white", "gray.800"),
      borderRadius: "sm",
      overflow: "hidden",
    },
    path: {
      fill: useColorModeValue("gray.100", "whiteAlpha.100"),
    },
  };

  const controlsStyles = {
    "&.react-flow__controls": {
      borderRadius: "sm",
      overflow: "hidden",
      shadow: "none",
    },
    "&.react-flow__controls > button:last-child": {
      borderBottom: "none",
    },
    ".react-flow__controls-button": {
      backgroundColor: useColorModeValue("gray.100", "whiteAlpha.200"),
      borderBottom: "none",
    },
    ".react-flow__controls-button > svg": {
      fill: useColorModeValue("gray.800", "whiteAlpha.900"),
    },
    ".react-flow__controls-button:hover": {
      backgroundColor: useColorModeValue("gray.200", "gray.600"),
    },
  };

  const nodeTypes = useMemo(
    () => ({
      product: ProductNode,
      manufacturer: ManufacturerNode,
      application: ApplicationNode,
      market: MarketNode,
      origin: OriginNode,
    }),
    [],
  );

  return (
    <>
      <Box
        h={reactFlowHeight}
        sx={{
          ".react-flow__handle": {
            width: "0.5rem",
            height: "0.5rem",
            backgroundColor: useColorModeValue("gray.200", "whiteAlpha.200"),
            borderColor: useColorModeValue("gray.200", "gray.600"),
          },
          ".react-flow__handle.react-flow__handle-top": {
            top: "-0.75rem",
          },
          ".react-flow__handle.react-flow__handle-right": {
            right: "-0.75rem",
          },
          ".react-flow__handle.react-flow__handle-bottom": {
            bottom: "-0.75rem",
          },
          ".react-flow__handle.react-flow__handle-left": {
            left: "-0.75rem",
          },
        }}
      >
        <ReactFlow
          proOptions={{ hideAttribution: true }}
          nodes={nodes}
          edges={edges}
          onConnect={onConnect}
          onNodesChange={onNodesChange}
          onEdgesChange={onEdgesChange}
          nodeTypes={nodeTypes}
        >
          <Background />
          <StyledControls sx={controlsStyles} />
          <StyledMiniMap sx={minimapStyles} />
          <Panel position={"top-left"}>
            <Button size={"sm"} leftIcon={<Icon as={FaPlus} />} onClick={onOpen}>
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
