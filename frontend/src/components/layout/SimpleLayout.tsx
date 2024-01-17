import { UserMenu } from "components/ui";
import View from "views";
import { useAuth } from "hooks";
import {
  Button,
  Heading,
  HStack,
  IconButton,
  Spacer,
  useColorMode,
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { MoonIcon, SunIcon } from "@chakra-ui/icons";

const SignInAndSignUp = () => {
  const { userManager, signOut } = useAuth();
  const { t } = useTranslation();
  const { colorMode, toggleColorMode } = useColorMode();
  return (
    <>
      <Button
        size={"sm"}
        variant={"unstyled"}
        onClick={() => void userManager.signinRedirect()}
      >
        {t("login")}
      </Button>
      <Button size={"sm"} variant={"unstyled"} onClick={() => signOut()}>
        {t("sign up")}
      </Button>
      <IconButton
        size={"sm"}
        variant={"unstyled"}
        aria-label={"Dark mode"}
        icon={colorMode === "dark" ? <SunIcon /> : <MoonIcon />}
        onClick={() => toggleColorMode()}
      />
    </>
  );
};

const SimpleLayout = () => {
  const isAuthenticated = useAuth((state) => state.isAuthenticated);

  return (
    <>
      <HStack
        as="header"
        position={"sticky"}
        top={0}
        spacing={4}
        px={4}
        borderBottom={"1px"}
      >
        <Heading size={"md"}>Broccoli</Heading>
        <Spacer />
        {isAuthenticated ? <UserMenu /> : <SignInAndSignUp />}
      </HStack>
      <View />
    </>
  );
};

export default SimpleLayout;
