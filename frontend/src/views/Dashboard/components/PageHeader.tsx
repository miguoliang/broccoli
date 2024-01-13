import { Heading, HStack, Spacer, Text, VStack } from "@chakra-ui/react";
import React from "react";

interface PageHeaderProps {
  title: string;
  description?: string;
  children?: React.ReactNode;
}

const PageHeader = React.forwardRef<HTMLDivElement, PageHeaderProps>(
  ({ title, description, children }, ref) => {
    return (
      <HStack pb={4} ref={ref}>
        <VStack flexShrink={0} alignItems={"left"}>
          <Heading
            fontWeight={"bold"}
            fontSize={"xl"}
            fontFamily={"sans-serif"}
          >
            {title}
          </Heading>
          {description && <Text color={"gray.500"}>{description}</Text>}
        </VStack>
        <Spacer />
        {children}
      </HStack>
    );
  },
);

export default PageHeader;
