import { Handle, NodeProps, Position, useNodesInitialized } from "reactflow";
import { GeneralNodeDataProps, NodeMode } from "./types";
import { memo, PropsWithChildren, useCallback, useEffect, useRef, useState } from "react";
import {
  Badge,
  Card,
  CardHeader,
  HStack,
  Icon,
  IconButton,
  Spacer,
  VStack,
} from "@chakra-ui/react";
import { colorSchemes } from "./constants";
import { useTranslation } from "react-i18next";
import { CheckIcon, CloseIcon, DeleteIcon } from "@chakra-ui/icons";
import { BiRename } from "react-icons/bi";
import useReactFlowStore from "../../../hooks/useReactFlowStore";
import EditableInput from "../EditableInput";

export type GeneralNodeProps = PropsWithChildren<NodeProps<GeneralNodeDataProps>>;

const GeneralNode = (props: GeneralNodeProps) => {
  const ref = useRef<HTMLInputElement>(null);
  const { t } = useTranslation();
  const nodesInitialized = useNodesInitialized({ includeHiddenNodes: false });
  const [nodeMode, setNodeMode] = useState<NodeMode>("renaming");

  useEffect(() => {
    if (nodesInitialized && nodeMode === "renaming") {
      ref.current?.focus();
    }
  }, [nodesInitialized, nodeMode]);

  const onRenameSubmit = useCallback(() => {
    const instance = useReactFlowStore.getState().instance;
    instance?.setNodes((nodes) => {
      const node = nodes.find((node) => node.id === props.id);
      if (node) {
        node.data = { ...node.data, name: ref.current!.value };
      }
      return nodes;
    });
    setNodeMode("normal");
  }, []);

  const onRenameCancel = useCallback(() => {
    setNodeMode("normal");
  }, []);

  return (
    <>
      <Card size={"xs"} colorScheme={colorSchemes[props.data.type]}>
        <CardHeader as={VStack} alignItems={"stretch"}>
          <HStack spacing={1}>
            <Badge fontSize={"xs"} colorScheme={colorSchemes[props.data.type]}>
              {t(props.data.type)}
            </Badge>
            <Spacer />
            <Toolbar
              mode={nodeMode}
              onRenameSubmit={onRenameSubmit}
              setMode={setNodeMode}
              onRenameCancel={onRenameCancel}
            />
          </HStack>
          <EditableInput
            ref={ref}
            value={props.data.name}
            isEditing={nodeMode === "renaming"}
            onEnter={onRenameSubmit}
            onEscape={onRenameCancel}
          />
          {props.children}
        </CardHeader>
      </Card>
      <Handle type="source" position={Position.Top} id="a" />
      <Handle type="source" position={Position.Right} id="b" />
      <Handle type="source" position={Position.Bottom} id="c" />
      <Handle type="source" position={Position.Left} id="d" />
    </>
  );
};

type ToolbarProps = Readonly<{
  mode?: NodeMode;
  setMode?: (mode: NodeMode) => void;
  onRenameSubmit?: () => void;
  onRenameCancel?: () => void;
  onDelete?: () => Promise<boolean>;
}>;

const Toolbar = ({
  mode = "normal",
  setMode,
  onRenameSubmit,
  onRenameCancel,
  onDelete,
}: ToolbarProps) => {
  const { t } = useTranslation();
  switch (mode) {
    case "renaming":
      return (
        <>
          <IconButton
            variant={"ghost"}
            title={"submit"}
            aria-label={"submit"}
            icon={<CheckIcon />}
            size={"2xs"}
            onClick={onRenameSubmit}
          />
          <IconButton
            variant={"ghost"}
            title={"cancel"}
            aria-label={"cancel"}
            icon={<CloseIcon />}
            size={"2xs"}
            onClick={onRenameCancel}
          />
        </>
      );
    case "normal":
    default:
      return (
        <>
          <IconButton
            variant={"ghost"}
            title={t("rename")}
            aria-label={"rename"}
            icon={<Icon as={BiRename} />}
            size={"2xs"}
            onClick={() => setMode?.("renaming")}
          />
          <IconButton
            variant={"ghost"}
            title={t("delete")}
            aria-label={"delete"}
            icon={<DeleteIcon />}
            size={"2xs"}
            onClick={onDelete}
          />
        </>
      );
  }
};

export default memo(GeneralNode);
