import { Badge, VStack } from "@chakra-ui/react";
import CustomEditable from "./CustomEditable";
import { GeneralNodeProps } from "./types";
import { useTranslation } from "react-i18next";
import { NodeProps } from "reactflow";
import { Field, FieldProps, Form, Formik } from "formik";
import useStore from "hooks/useReactFlowState";

const CustomNodeHeader = ({ id, type, data }: NodeProps<GeneralNodeProps>) => {
  const { t } = useTranslation();
  return (
    <VStack gap={1}>
      <Badge fontSize={"xs"}>{t(type)}</Badge>
      <Formik<GeneralNodeProps>
        initialValues={{ ...data }}
        onSubmit={(values, formikHelpers) => {
          formikHelpers.setSubmitting(true);
          formikHelpers.resetForm();
          useStore
            .getState()
            .setNodeData(id, values)
            .then(() => formikHelpers.setSubmitting(false))
            .catch((err) => {
              throw err;
            });
        }}
      >
        <Form>
          <Field name="name">
            {(props: FieldProps<string, GeneralNodeProps>) => (
              <CustomEditable
                startWithEditView={props.form.values.id === "temp-id"}
                {...props}
                submitForm
              />
            )}
          </Field>
        </Form>
      </Formik>
    </VStack>
  );
};

export default CustomNodeHeader;
