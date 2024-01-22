import { Editable, EditableInput, EditablePreview, HStack, Input } from "@chakra-ui/react";
import EditableControls from "../EditableControls";

interface CustomEditableProps {
  defaultValue: string;
}

const CustomEditable = ({ defaultValue }: CustomEditableProps) => {
  return (
    <Editable
      textAlign="center"
      defaultValue={defaultValue}
      fontSize="sm"
      isPreviewFocusable={false}
      as={HStack}
    >
      <EditablePreview />
      {/* Here is the custom input */}
      <Input size={"sm"} as={EditableInput} />
      <EditableControls />
    </Editable>
  );
};

export default CustomEditable;
