import { Handle, NodeProps, Position, useNodesInitialized } from "reactflow";
import { GeneralNodeDataProps } from "./types";
import { memo, PropsWithChildren, useEffect, useRef } from "react";
import {
  Badge,
  Card,
  CardHeader,
  Editable,
  EditableInput,
  EditablePreview,
  HStack,
  Input,
} from "@chakra-ui/react";
import { colorSchemes } from "./constants";
import EditableControls from "../EditableControls";

export type GeneralNodeProps = PropsWithChildren<NodeProps<GeneralNodeDataProps>>;

const GeneralNode = (props: GeneralNodeProps) => {
  const ref = useRef<HTMLInputElement>(null);
  const nodesInitialized = useNodesInitialized({ includeHiddenNodes: false });
  useEffect(() => {
    if (nodesInitialized) {
      ref.current?.focus();
    }
  }, [nodesInitialized]);

  return (
    <>
      <Card size={"xs"} colorScheme={colorSchemes[props.data.type]}>
        <CardHeader as={Badge} colorScheme={colorSchemes[props.data.type]} m={1}>
          <Editable as={HStack} startWithEditView isPreviewFocusable={false}>
            <EditablePreview flexGrow={1} cursor={"arrow"} />
            <Input size={"sm"} ref={ref} as={EditableInput} />
            <EditableControls />
          </Editable>
        </CardHeader>
      </Card>
      <Handle type="source" position={Position.Top} id="a" />
      <Handle type="source" position={Position.Right} id="b" />
      <Handle type="source" position={Position.Bottom} id="c" />
      <Handle type="source" position={Position.Left} id="d" />
    </>
  );
};

export default memo(GeneralNode);
