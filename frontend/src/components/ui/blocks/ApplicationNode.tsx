import { Handle, NodeProps, Position } from "reactflow";
import { GeneralNodeProps } from "./types";
import { memo, useRef } from "react";

const ApplicationNode = (props: NodeProps<GeneralNodeProps>) => {
  const ref = useRef<HTMLInputElement>(null);
  return (
    <>
      <input
        ref={ref}
        onFocus={() => console.log("onfocus")}
        onBlur={() => console.log("onblur")}
        onBlurCapture={() => console.log("onblur capture")}
      />
      <Handle type="source" position={Position.Top} id="a" />
      <Handle type="source" position={Position.Right} id="b" />
      <Handle type="source" position={Position.Bottom} id="c" />
      <Handle type="source" position={Position.Left} id="d" />
    </>
  );
};

export default memo(ApplicationNode);