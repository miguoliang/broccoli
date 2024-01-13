import {
  Button,
  Divider,
  FormControl,
  FormErrorMessage,
  FormLabel,
  GridItem,
  HStack,
  Input,
  SimpleGrid,
  Spacer,
  Text,
} from "@chakra-ui/react";
import { Field, Formik } from "formik";
import * as Yup from "yup";
import { useAuth } from "hooks";
import find from "lodash-es/find";
import { useQueryClient } from "@tanstack/react-query";
import { usePostAccountChangePassword } from "services";

interface AwsCognitoIdentity {
  userId: string;
  providerName: "Google" | "Facebook" | "LoginWithAmazon" | "Apple" | "OIDC";
  providerType: "Google" | "Facebook" | "LoginWithAmazon" | "Apple" | "OIDC";
  issuer: string | null;
  primary: "true" | "false";
  dateCreated: string;
}

const ChangePasswordSchema = Yup.object().shape({
  currentPassword: Yup.string().required("Required"),
  newPassword: Yup.string().required("Required"),
  confirmNewPassword: Yup.string().oneOf(
    [Yup.ref("newPassword"), null],
    "Passwords must match",
  ),
});

const Password = () => {
  const queryClient = useQueryClient();
  const user = useAuth((state) => state.user);
  const userPrimaryIdentity = find(
    user?.profile.identities as Array<AwsCognitoIdentity>,
    (value) => value.primary === "true",
  );
  if (userPrimaryIdentity?.providerType === "Google") {
    return (
      <Text>
        Users who log in through a third party cannot change their passwords
      </Text>
    );
  }

  return (
    <>
      <Text color={"gray.500"}>
        Enter your current & new password to reset your password
      </Text>
      <Formik
        initialValues={{
          currentPassword: "",
          newPassword: "",
          confirmNewPassword: "",
        }}
        validationSchema={ChangePasswordSchema}
        onSubmit={(values) =>
          queryClient.fetchQuery({
            queryKey: usePostAccountChangePassword.getKey({
              previousPassword: values.currentPassword,
              proposedPassword: values.newPassword,
            }),
            queryFn: usePostAccountChangePassword.queryFn,
          })
        }
      >
        {({ handleSubmit, handleReset, errors, touched }) => (
          <form onSubmit={handleSubmit} onReset={handleReset}>
            <FormControl
              paddingY={8}
              isInvalid={!!errors.currentPassword && touched.currentPassword}
            >
              <SimpleGrid columns={3} alignItems={"center"} gap={3}>
                <FormLabel htmlFor={"currentPassword"}>
                  Current Password
                </FormLabel>
                <GridItem colSpan={2} position={"relative"}>
                  <Field
                    as={Input}
                    name={"currentPassword"}
                    id={"currentPassword"}
                    type={"password"}
                    maxWidth={"700px"}
                  />
                  <FormErrorMessage>
                    {errors.currentPassword ?? ""}
                  </FormErrorMessage>
                </GridItem>
              </SimpleGrid>
            </FormControl>
            <Divider />
            <FormControl
              paddingY={8}
              isInvalid={!!errors.newPassword && touched.newPassword}
            >
              <SimpleGrid columns={3} alignItems={"center"} gap={3}>
                <FormLabel htmlFor={"newPassword"}>New Password</FormLabel>
                <GridItem colSpan={2} position={"relative"}>
                  <Field
                    as={Input}
                    name={"newPassword"}
                    id={"newPassword"}
                    type={"password"}
                    maxWidth={"700px"}
                  />
                  <FormErrorMessage>
                    {errors.newPassword ?? ""}
                  </FormErrorMessage>
                </GridItem>
              </SimpleGrid>
            </FormControl>
            <Divider />
            <FormControl
              paddingY={8}
              isInvalid={
                !!errors.confirmNewPassword && touched.confirmNewPassword
              }
            >
              <SimpleGrid columns={3} alignItems={"center"} gap={3}>
                <FormLabel htmlFor={"confirmNewPassword"}>
                  Confirm Password
                </FormLabel>
                <GridItem colSpan={2} position={"relative"}>
                  <Field
                    as={Input}
                    name={"confirmNewPassword"}
                    id={"confirmNewPassword"}
                    type={"password"}
                    maxWidth={"700px"}
                  />
                  <FormErrorMessage>
                    {errors.confirmNewPassword ?? ""}
                  </FormErrorMessage>
                </GridItem>
              </SimpleGrid>
            </FormControl>
            <Divider />
            <HStack mt={4}>
              <Spacer />
              <Button type={"reset"} variant={"outline"}>
                Reset
              </Button>
              <Button type={"submit"} colorScheme={"blue"}>
                Save Changes
              </Button>
            </HStack>
          </form>
        )}
      </Formik>
    </>
  );
};

export default Password;
