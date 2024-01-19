import { ButtonGroup, Flex, IconButton, useEditableControls } from "@chakra-ui/react";
import { CheckIcon, CloseIcon, EditIcon } from "@chakra-ui/icons";

const EditableControls = () => {
  const { isEditing, getSubmitButtonProps, getCancelButtonProps, getEditButtonProps } =
    useEditableControls();
  const submitButtonProps = getSubmitButtonProps();
  const cancelButtonProps = getCancelButtonProps();
  return isEditing ? (
    <ButtonGroup justifyContent="center" size="sm">
      <IconButton icon={<CheckIcon />} {...submitButtonProps} aria-label={"check"} />
      <IconButton icon={<CloseIcon />} {...cancelButtonProps} aria-label={"close"} />
    </ButtonGroup>
  ) : (
    <Flex justifyContent="center">
      <IconButton size="sm" icon={<EditIcon />} {...getEditButtonProps()} aria-label={"edit"} />
    </Flex>
  );
};

export default EditableControls;
