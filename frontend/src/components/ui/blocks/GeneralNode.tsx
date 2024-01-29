import { Handle, NodeProps, Position, useNodesInitialized } from "reactflow";
import { GeneralNodeDataProps } from "./types";
import {
  ChangeEventHandler,
  forwardRef,
  memo,
  PropsWithChildren,
  useCallback,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from "react";
import {
  Badge,
  Card,
  CardHeader,
  Heading,
  HStack,
  Icon,
  IconButton,
  Input,
  Spacer,
  VStack,
} from "@chakra-ui/react";
import { colorSchemes } from "./constants";
import { useTranslation } from "react-i18next";
import { CheckIcon, CloseIcon, DeleteIcon } from "@chakra-ui/icons";
import { BiRename } from "react-icons/bi";

export type GeneralNodeProps = PropsWithChildren<NodeProps<GeneralNodeDataProps>>;

const GeneralNode = (props: GeneralNodeProps) => {
  const ref = useRef<EditableInputRef>(null);
  const { t } = useTranslation();
  const nodesInitialized = useNodesInitialized({ includeHiddenNodes: false });
  const [nodeMode, setNodeMode] = useState<NodeMode>("renaming");

  useEffect(() => {
    if (nodesInitialized && nodeMode === "renaming") {
      ref.current?.inputRef?.focus();
    }
  }, [nodesInitialized, nodeMode]);

  const onRenameSubmit = useCallback(async () => {
    setNodeMode("normal");
    return new Promise<boolean>(() => true);
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
          <EditableInput ref={ref} mode={nodeMode} />
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

type NodeMode = "renaming" | "normal";

type ToolbarProps = Readonly<{
  mode?: NodeMode;
  setMode?: (mode: NodeMode) => void;
  onRenameSubmit?: () => Promise<boolean>;
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

type EditableInputProps = Readonly<{
  value?: string;
  mode?: NodeMode;
  onChange?: ChangeEventHandler<HTMLInputElement>;
}>;

type EditableInputRef = {
  inputRef: HTMLInputElement | null;
};

const EditableInput = forwardRef<EditableInputRef, EditableInputProps>(
  ({ value = "", mode = "renaming", onChange }, ref) => {
    const inputRef = useRef<HTMLInputElement>(null);
    useImperativeHandle(ref, () => ({
      inputRef: inputRef.current,
    }));
    return mode === "renaming" ? (
      <Input
        ref={inputRef}
        size={"xs"}
        defaultValue={value}
        textAlign={"center"}
        onChange={onChange}
      />
    ) : (
      <Heading as={"h6"}>{value}</Heading>
    );
  },
);

export default memo(GeneralNode);
