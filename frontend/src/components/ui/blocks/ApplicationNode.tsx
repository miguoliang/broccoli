import EditableControls from "../EditableControls";
import { Editable, EditableInput, EditablePreview, Input } from "@chakra-ui/react";

const ApplicationNode = () => {
  return (
    <Editable
      textAlign="center"
      defaultValue="Rasengan ⚡️"
      fontSize="2xl"
      isPreviewFocusable={false}
    >
      <EditablePreview />
      {/* Here is the custom input */}
      <Input as={EditableInput} />
      <EditableControls />
    </Editable>
  );
};

export default ApplicationNode