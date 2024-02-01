import ReactFlow, { Background, ConnectionMode, Node, Panel, ReactFlowProvider } from "reactflow";
import { DragEvent, DragEventHandler, useCallback } from "react";
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
import { StyledControls, StyledMiniMap } from "../components/ui";
import { colorSchemes, GeneralNodeDataProps, NodeType } from "../components/ui/blocks";
import { AnimatePresence, motion, MotionConfig } from "framer-motion";
import BiDirectionalEdge from "../components/ui/BiDirectionalEdge";
import ApplicationNode from "../components/ui/blocks/ApplicationNode";
import useReactFlowStore from "../hooks/useReactFlowStore";
import ManufacturerNode from "../components/ui/blocks/ManufacturerNode";
import ProductNode from "../components/ui/blocks/ProductNode";
import OriginNode from "../components/ui/blocks/OriginNode";
import MarketNode from "../components/ui/blocks/MarketNode";

const nodeTypes = {
  application: ApplicationNode,
  manufacturer: ManufacturerNode,
  product: ProductNode,
  origin: OriginNode,
  market: MarketNode,
};

const edgeTypes = {
  default: BiDirectionalEdge,
}

const Home = () => {
  return (
    <Box
      h={"calc(100vh - 2.5rem)"}
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
        ".react-flow__controls": {
          borderRadius: "sm",
          overflow: "hidden",
          shadow: "none",
        },
        ".react-flow__controls > button:last-child": {
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
        ".react-flow__minimap": {
          backgroundColor: useColorModeValue("white", "gray.800"),
          borderRadius: "sm",
          overflow: "hidden",
        },
        ".react-flow__minimap path": {
          fill: useColorModeValue("gray.100", "whiteAlpha.100"),
        },
      }}
    >
      <ReactFlowProvider>
        <Flow />
      </ReactFlowProvider>
    </Box>
  );
};

const AddBlockWidget = () => {
  const { t } = useTranslation();
  const { isOpen, onToggle } = useDisclosure();
  const bg = useColorModeValue("gray.100", "whiteAlpha.200");
  return (
    <MotionConfig transition={{ duration: 0.1 }}>
      <Box bg={bg} overflow={"hidden"}>
        <Button
          tabIndex={-1}
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
              key={"list"}
              as={motion.ul}
              spacing={2}
              pb={2}
              initial={{ opacity: 0, height: 0, width: 0 }}
              animate={{ opacity: 1, height: "auto", width: "auto" }}
              exit={{ opacity: 0, height: 0, width: 0 }}
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
    </MotionConfig>
  );
};

interface DragBlockWidgetProps {
  type: NodeType;
}

const DragBlockWidget = ({ type }: DragBlockWidgetProps) => {
  const { t } = useTranslation();

  const onDragStart = (event: DragEvent, nodeType: NodeType) => {
    event.dataTransfer.setData("application/reactflow", nodeType);
    event.dataTransfer.effectAllowed = "move";
  };

  return (
    <Card
      size={"sm"}
      colorScheme={colorSchemes[type]}
      mx={2}
      onDragStart={(event) => onDragStart(event, type)}
      draggable
    >
      <CardHeader overflowWrap={"initial"}>{t(type)}</CardHeader>
    </Card>
  );
};

let id = 0;

const Flow = () => {
  const onDragOver = useCallback<DragEventHandler<HTMLDivElement>>((event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = "move";
  }, []);

  const onDrop = useCallback<DragEventHandler<HTMLDivElement>>((event) => {
    event.preventDefault();
    const type = event.dataTransfer.getData("application/reactflow") as NodeType;
    const reactFlowInstance = useReactFlowStore.getState().instance;
    // check if the dropped element is valid
    if (typeof type === "undefined" || !type || !reactFlowInstance) {
      return;
    }
    // reactFlowInstance.project was renamed to reactFlowInstance.screenToFlowPosition,
    // so you don't need to subtract the reactFlowBounds.left/top anymore
    // details: https://reactflow.dev/whats-new/2023-11-10
    const position = reactFlowInstance.screenToFlowPosition({
      x: event.clientX,
      y: event.clientY,
    });
    const newNode: Node<GeneralNodeDataProps> = {
      id: `${type}-${id++}`,
      type,
      position,
      focusable: true,
      data: { id: `${type}-${id++}`, name: "", type },
    };
    reactFlowInstance.addNodes(newNode);
  }, []);



  return (
    <ReactFlow
      proOptions={{ hideAttribution: true }}
      defaultNodes={[]}
      defaultEdges={[]}
      onDrop={onDrop}
      onDragOver={onDragOver}
      onInit={(instance) => useReactFlowStore.setState({ instance })}
      nodeTypes={nodeTypes}
      edgeTypes={edgeTypes}
      connectionMode={ConnectionMode.Loose}
    >
      <Background />
      <StyledControls />
      <StyledMiniMap zoomable pannable />
      <Panel position={"top-left"}>
        <AddBlockWidget />
      </Panel>
    </ReactFlow>
  );
};

export default Home;
