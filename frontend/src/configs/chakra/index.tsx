import { extendTheme } from "@chakra-ui/react";
import first from "lodash-es/first";
import join from "lodash-es/join";
import mapValues from "lodash-es/mapValues";

import resolveConfig from "tailwindcss/resolveConfig";
import tailwindConfig from "../../../tailwind.config";
import { modalTheme } from "./modal";

const tailwindTheme = resolveConfig(tailwindConfig).theme;

function addMissingBaseField(
  obj?: { DEFAULT?: unknown; [key: string]: unknown },
  value?: unknown,
): unknown {
  return {
    base: value ?? obj?.DEFAULT,
    ...obj,
  };
}

export default extendTheme({
  initialColorMode: "system",
  useSystemColorMode: true,
  blur: addMissingBaseField(tailwindTheme.blur),
  breakpoints: addMissingBaseField(tailwindTheme.screens, "0px"),
  colors: tailwindTheme.colors,
  radius: addMissingBaseField(tailwindTheme.borderRadius),
  shadows: addMissingBaseField(tailwindTheme.boxShadow),
  space: addMissingBaseField(tailwindTheme.spacing),
  sizes: {
    container: tailwindTheme.container,
    ...tailwindTheme.spacing,
    ...tailwindTheme.maxWidth,
  },
  transition: {
    duration: tailwindTheme.transitionDuration,
    easing: tailwindTheme.transitionTimingFunction,
    property: tailwindTheme.transitionProperty,
  },
  letterSpacings: tailwindTheme.letterSpacing,
  lineHeights: tailwindTheme.lineHeight,
  fontWeights: tailwindTheme.fontWeight,
  fonts: mapValues(tailwindTheme.fontFamily, (value) => join(value, ",")),
  fontSizes: mapValues(tailwindTheme.fontSize, first),
  zIndices: tailwindTheme.zIndex,
  components: {
    Modal: modalTheme,
  },
});
