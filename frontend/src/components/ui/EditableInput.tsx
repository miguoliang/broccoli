import {
  ChangeEventHandler,
  forwardRef,
  KeyboardEventHandler,
  PropsWithChildren,
  useCallback,
} from "react";
import { Heading, HStack, Input } from "@chakra-ui/react";

type EditableInputProps = Readonly<
  PropsWithChildren<{
    value?: string;
    isEditing?: boolean;
    onChange?: ChangeEventHandler<HTMLInputElement>;
    onEnter?: () => void;
    onEscape?: () => void;
  }>
>;

const EditableInput = forwardRef<HTMLInputElement, EditableInputProps>(
  ({ value = "", isEditing = false, children, onChange, onEnter, onEscape }, ref) => {
    const onKeyDown = useCallback<KeyboardEventHandler<HTMLInputElement>>(
      (e) => {
        if (e.key === "Enter") {
          onEnter?.();
          e.currentTarget.blur();
        } else if (e.key === "Escape") {
          onEscape?.();
          e.currentTarget.blur();
        }
      },
      [onEnter, onEscape],
    );

    return (
      <HStack>
        {isEditing ? (
          <Input
            ref={ref}
            size={"xs"}
            defaultValue={value}
            textAlign={"center"}
            onChange={onChange}
            onKeyDown={onKeyDown}
          />
        ) : (
          <Heading size={"md"} mx={"auto"} as={"h6"}>
            {value}
          </Heading>
        )}
        {children}
      </HStack>
    );
  },
);

export default EditableInput;
