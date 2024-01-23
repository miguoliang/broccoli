import ReactFlow, { Background, Panel } from "reactflow";
import { useSizes } from "@chakra-ui/react-use-size";
import { useMemo } from "react";
import {
  Box,
  Button,
  Card,
  CardHeader,
  Icon,
  List,
  ListItem,
  useColorModeValue,
  useDisclosure,
} from "@chakra-ui/react";
import { FaPlus } from "react-icons/fa6";
import { useTranslation } from "react-i18next";
import "reactflow/dist/style.css";
import useStore from "hooks/useReactFlowState";
import { StyledControls, StyledMiniMap } from "../components/ui";
import {
  ApplicationNode,
  colorSchemes,
  ManufacturerNode,
  MarketNode,
  NodeType,
  OriginNode,
  ProductNode,
} from "../components/ui/blocks";
import { AnimatePresence, motion } from "framer-motion";

const Home = () => {
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
          <AddBlockWidget />
        </Panel>
      </ReactFlow>
    </Box>
  );
};

const AddBlockWidget = () => {
  const { t } = useTranslation();
  const { isOpen, onToggle } = useDisclosure();
  const bg = useColorModeValue("gray.100", "whiteAlpha.200");
  return (
    <Box bg={bg} overflow={"hidden"} transition={"all 0.2s ease-in-out"}>
      <Button
        size={"sm"}
        variant={"plain"}
        leftIcon={
          <Icon
            as={FaPlus}
            transform={`rotate(${isOpen ? "45deg" : "0deg"})`}
            transition={"transform 0.1s ease-in-out"}
          />
        }
        onClick={onToggle}
      >
        {t("block")}
      </Button>
      <AnimatePresence>
        {isOpen && (
          <List
            as={motion.ul}
            spacing={2}
            pb={2}
            initial={{ opacity: 0, height: 0, width: 0 }}
            animate={{ opacity: 1, height: "auto", width: "auto" }}
            exit={{ opacity: 0, height: 0, width: 0 }}
            transition={{ duration: 2, ease: "easeOut" }}
          >
            <ListItem as={DragBlockWidget} type={"application"} />
            <ListItem as={DragBlockWidget} type={"manufacturer"} />
            <ListItem as={DragBlockWidget} type={"market"} />
            <ListItem as={DragBlockWidget} type={"origin"} />
            <ListItem as={DragBlockWidget} type={"product"} />
          </List>
        )}
      </AnimatePresence>
    </Box>
  );
};

interface DragBlockWidgetProps {
  type: NodeType;
}

const DragBlockWidget = ({ type }: DragBlockWidgetProps) => {
  const { t } = useTranslation();
  return (
    <Card size={"sm"} colorScheme={colorSchemes[type]} mx={2}>
      <CardHeader overflowWrap={"initial"}>{t(type)}</CardHeader>
    </Card>
  );
};

export default Home;
