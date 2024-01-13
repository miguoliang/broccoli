import { formErrorAnatomy as parts } from "@chakra-ui/anatomy";
import {
  createMultiStyleConfigHelpers,
  defineStyle,
} from "@chakra-ui/styled-system";

const helpers = createMultiStyleConfigHelpers(parts.keys);

const baseStyleText = defineStyle({
  position: "absolute",
  mt: 0,
  bottom: "-21px",
});

const baseStyleIcon = defineStyle({
  position: "absolute",
  marginEnd: 0,
  bottom: "-21px",
});

const baseStyle = helpers.definePartsStyle({
  text: baseStyleText,
  icon: baseStyleIcon,
});

export const formErrorTheme = helpers.defineMultiStyleConfig({
  baseStyle,
});
