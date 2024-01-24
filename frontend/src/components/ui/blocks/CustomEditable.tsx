import { Editable, EditableInput, EditablePreview, HStack, Input } from "@chakra-ui/react";
import EditableControls from "../EditableControls";
import { FieldProps } from "formik";

type CustomEditableProps = {
  startWithEditView?: boolean;
  submitForm?: boolean;
} & FieldProps<string>;

const CustomEditable = ({ field, form, startWithEditView, submitForm }: CustomEditableProps) => {
  return (
    <Editable
      textAlign={"center"}
      fontSize={"sm"}
      isPreviewFocusable={false}
      startWithEditView={startWithEditView}
      submitOnBlur={true}
      as={HStack}
      onSubmit={() => submitForm && void form.submitForm()}
      defaultValue={field.value}
    >
      <EditablePreview />
      {/* Here is the custom input */}
      <Input size={"sm"} as={EditableInput} {...field} />
      <EditableControls />
    </Editable>
  );
};

export default CustomEditable;
