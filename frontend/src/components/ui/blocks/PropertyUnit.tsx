import { IconButton, Text, useBoolean } from "@chakra-ui/react";
import { useRef } from "react";
import EditableInput from "../EditableInput";
import { CheckIcon, CloseIcon, EditIcon, RepeatClockIcon } from "@chakra-ui/icons";
import { useSetProperty } from "../../../gens/backend/api";
import { useNodeId } from "reactflow";

type PropertyUnitProps = {
  title: string;
  key: string;
  value: string;
};

const PropertyUnit = ({ title, key, value }: PropertyUnitProps) => {
  const [isEditing, setEditing] = useBoolean(false);
  const ref = useRef<HTMLInputElement>(null);
  const nodeId = useNodeId();
  const mutation = useSetProperty();
  const onSubmit = () => {
    if (!nodeId) {
      return;
    }
    mutation.mutate({
      id: nodeId,
      data: {
        key,
        value: ref.current?.value ?? "",
        scope: "default",
      },
    });
    setEditing.off();
  };
  const onCancel = () => setEditing.off();
  return (
    <>
      <Text>{title}</Text>
      <EditableInput
        ref={ref}
        isEditing={isEditing}
        value={value}
        onEnter={onSubmit}
        onEscape={onCancel}
      >
        {!isEditing && (
          <IconButton
            variant={"ghost"}
            aria-label={"edit"}
            icon={<EditIcon />}
            size={"2xs"}
            onClick={setEditing.on}
          />
        )}
        {!isEditing && (
          <IconButton
            variant={"ghost"}
            aria-label={"history"}
            icon={<RepeatClockIcon />}
            size={"2xs"}
          />
        )}
        {isEditing && (
          <IconButton variant={"ghost"} aria-label={"submit"} icon={<CheckIcon />} size={"2xs"} />
        )}
        {isEditing && (
          <IconButton
            variant={"ghost"}
            aria-label={"cancel"}
            icon={<CloseIcon />}
            size={"2xs"}
            onClick={setEditing.off}
          />
        )}
      </EditableInput>
    </>
  );
};

export default PropertyUnit;