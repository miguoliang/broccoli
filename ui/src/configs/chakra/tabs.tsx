import { tabsAnatomy } from "@chakra-ui/anatomy";
import { createMultiStyleConfigHelpers } from "@chakra-ui/react";

const helpers = createMultiStyleConfigHelpers(tabsAnatomy.keys);

// define the base component styles
const variantLine = helpers.definePartsStyle({
  tablist: {
    borderColor: "gray.100",
  },
  tab: {
    color: "gray.400",
    _selected: {
      color: "blue.500",
      borderBottomColor: "blue.500",
    },
  },
});

// export the component theme
export const tabsTheme = helpers.defineMultiStyleConfig({
  variants: { line: variantLine },
});
