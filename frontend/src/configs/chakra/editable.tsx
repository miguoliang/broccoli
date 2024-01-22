/**
 * Year in, year out, we find that the problems we face are the same ones we saw before, and
 * the solutions we propose are the same old ones we have tried many times.
 */
import { editableAnatomy as parts } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/styled-system";

const helpers = createMultiStyleConfigHelpers(parts.keys);

const baseStyle = helpers.definePartsStyle({
  preview: {
    py: 0,
  },
});

export const editableTheme = helpers.defineMultiStyleConfig({
  baseStyle,
});
